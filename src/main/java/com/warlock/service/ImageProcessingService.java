package com.warlock.service;

import com.google.common.io.ByteStreams;
import com.warlock.domain.TempImageUrl;
import com.warlock.model.enums.ImageStatus;
import com.warlock.model.records.ImageInfo;
import com.warlock.repository.TempImageUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "images")
public class ImageProcessingService {

    private final MinioStorageService storageService;
    private final TempImageUrlRepository tempImageUrlRepository;

    private final UserService userService;
    private final ExtinctService extinctService;

    @Value("${image.small_thumbnail.size}")
    private Integer smallSize;

    @Value("${image.medium_thumbnail.size}")
    private Integer mediumSize;

    @Value("${image.large_thumbnail.size}")
    private Integer largeSize;


    /**
     * Обработать файл как изображение
     *
     * @param file файл изображения
     * @param directory каталог сохранения
     * @return ImageInfo с ссылками на изображения и его миниатюры
     * @throws IOException ошибка сохранения
     */
    public ImageInfo processImage(MultipartFile file, String directory) throws IOException
    {
        String originalPath = storageService.uploadFile(file, directory);

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        log.info("Creating thumbnails");
        // Миниатюры
        String smallThumbnailUrl = createThumbnail(originalImage, file, smallSize, directory);
        log.info("Small thumbnail : {}", smallThumbnailUrl);
        String mediumThumbnailUrl = createThumbnail(originalImage, file, mediumSize, directory);
        log.info("Medium thumbnail : {}", mediumThumbnailUrl);
        String largeThumbnailUrl = createThumbnail(originalImage, file, largeSize, directory);
        log.info("Large thumbnail : {}", largeThumbnailUrl);

        return new ImageInfo(originalPath, smallThumbnailUrl, mediumThumbnailUrl, largeThumbnailUrl);
    }


    /**
     * Создать миниатюры
     *
     * @param originalImage Оригинальное изображение
     * @param file Оригинальный файл
     * @param size Размер миниатюры в пикселях
     * @param directory Каталог сохранения
     * @return Путь до сохранённой в S3 миниатюры
     * @throws IOException ошибка сохранения
     */
    private String createThumbnail(BufferedImage originalImage,
                                   MultipartFile file,
                                   Integer size,
                                   String directory) throws IOException
    {
        BufferedImage thumbnail = Scalr.resize(originalImage, size);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String formatName = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        ImageIO.write(thumbnail, formatName, os);

        MultipartFile thumbnailFile = new ByteArrayMultipartFile(
                "thumbnail-" + size + "." + formatName,
                file.getOriginalFilename(),
                file.getContentType(),
                os.toByteArray()
        );

        return storageService.uploadFile(thumbnailFile, directory);
    }

    /**
     * Сохранить ссылку во временной таблице
     *
     * @param originalUrl ссылка на изображение
     * @param targetEntity User или Extinct
     * @param targetId ID сущности
     */
    @Transactional
    public void saveUrl(String originalUrl, String targetEntity, Long targetId){
        TempImageUrl tempImageUrl = new TempImageUrl()
                .setOriginalUrl(originalUrl)
                .setTargetEntity(targetEntity)
                .setTargetEntityId(targetId)
                .setExpiresAt(LocalDateTime.now().plusMinutes(2))
                .setStatus(ImageStatus.PENDING)
                .setRetryCount(0);
        log.info("Save pending image {} in temp table", tempImageUrl);
        tempImageUrlRepository.save(tempImageUrl);
    }

    /**
     * Обработка PENDING изображений
     */
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void processPendingImages(){
        List<TempImageUrl> pendingUrls = tempImageUrlRepository
                .findByStatusAndExpiresAtAfter(ImageStatus.PENDING, LocalDateTime.now());

        pendingUrls.forEach(url ->{
            try{
                var images = processImageFromUrl(url);
                url.setStatus(ImageStatus.COMPLETED);
                log.info("Successfully upload image from URL to S3");
                updateEntityWithImages(url.getTargetEntity(), url.getTargetEntityId(), images);
                log.info("Successfully update images in entities");
            } catch (Exception e){
                log.error("Failed to process image URL: {}", e.getMessage());
                url.setRetryCount(url.getRetryCount() + 1);

                if (url.getRetryCount() >= 5){
                    log.error("Uploading image failed after 5 attempts");
                    url.setStatus(ImageStatus.FAILED);
                }
            }
        });

        tempImageUrlRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    /**
     * Обработка сообщений по URL
     *
     * @param tempImageUrl URL изображения
     * @return ImageInfo
     * @throws IOException ошибка скачивания изображения по URL
     */
    @Cacheable(key = "'url:' + #originalUrl")
    public ImageInfo processImageFromUrl(TempImageUrl tempImageUrl) throws IOException
    {
        URL url = new URL(tempImageUrl.getOriginalUrl());
        Path tempFile = Files.createTempFile("download-", ".tmp");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String contentType = connection.getContentType();

            if (contentType == null || !contentType.startsWith("image/")){
                throw new IOException("Invalid image content type: " + contentType);
            }

            try (
                    InputStream is = connection.getInputStream();
                    OutputStream os = Files.newOutputStream(tempFile)
            ){
                ByteStreams.copy(is, os);
            }

            MultipartFile multipartFile = new ByteArrayMultipartFile(
                    "image",
                    FilenameUtils.getName(url.getPath()),
                    contentType,
                    Files.readAllBytes(tempFile)
            );


            String directory = tempImageUrl.getTargetEntity().equalsIgnoreCase("USER") ?
                    "avatars" :
                    "extincts";

            var images = processImage(multipartFile, directory);
            Files.deleteIfExists(tempFile);
            return images;
        } catch (IOException e){
            Files.deleteIfExists(tempFile);
            throw e;
        }
    }

    /**
     * Обновить информацию об изображениях соответствующей сущности
     *
     * @param targetEntity сущность (User или Extinct)
     * @param targetEntityId ID сущности
     * @param imageInfo все ссылки с изображениями
     */
    private void updateEntityWithImages(String targetEntity, Long targetEntityId, ImageInfo imageInfo) {
        if ("user".equals(targetEntity)) {
            userService.updateUserImage(targetEntityId,
                    imageInfo.originalUrl(),
                    imageInfo.smallThumbnailUrl(),
                    imageInfo.mediumThumbnailUrl(),
                    imageInfo.largeThumbnailUrl());
        } else if ("extinct".equals(targetEntity)) {
            extinctService.updateExtinctImage(targetEntityId,
                    imageInfo.originalUrl(),
                    imageInfo.smallThumbnailUrl(),
                    imageInfo.mediumThumbnailUrl(),
                    imageInfo.largeThumbnailUrl());
        }
    }


    /**
     * Получить расширение файла
     *
     * @param filename имя файла
     * @return расширение файла
     */
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    // Вспомогательный класс для создания MultipartFile из byte[]
    static class ByteArrayMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public ByteArrayMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @NotNull
        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return originalFilename; }
        @Override public String getContentType() { return contentType; }
        @Override public boolean isEmpty() { return content == null || content.length == 0; }
        @Override public long getSize() { return content.length; }
        @NotNull
        @Override public byte[] getBytes() { return content; }
        @NotNull
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(content); }
        @Override public void transferTo(@NotNull java.io.File dest) throws IOException, IllegalStateException {
            new java.io.FileOutputStream(dest).write(content);
        }
    }
}
