package com.warlock.service;

import com.warlock.exceptions.ImageProcessingException;
import com.warlock.model.records.ImageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessingServiceImpl implements ImageProcessingService {
    @Value("${image.small_thumbnail.size}")
    private Integer smallSize;

    @Value("${image.medium_thumbnail.size}")
    private Integer mediumSize;

    @Value("${image.large_thumbnail.size}")
    private Integer largeSize;

    private final ImageStorageService storageService;

    @Override
    public ImageInfo processImage(MultipartFile file, String directory){
        try {
            // 1. Валидация изображения
            validateImage(file);

            // 2. Генерация уникального имени файла
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String baseName = UUID.randomUUID().toString();

            // 3. Чтение оригинального изображения
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new IllegalArgumentException("Invalid image file");
            }

            // 4. Создание миниатюр
            byte[] smallImageBytes = resizeImage(originalImage, smallSize, fileExtension);
            byte[] mediumImageBytes = resizeImage(originalImage, mediumSize, fileExtension);
            byte[] largeImageBytes = resizeImage(originalImage, largeSize, fileExtension);

            // 5. Сохранение всех версий
            String originalUrl = storageService.saveImage(file, directory, baseName + "_original" + fileExtension);
            String smallUrl = storageService.saveImage(smallImageBytes, directory, baseName + "_small" + fileExtension);
            String mediumUrl = storageService.saveImage(mediumImageBytes, directory, baseName + "_medium" + fileExtension);
            String largeUrl = storageService.saveImage(largeImageBytes, directory, baseName + "_large" + fileExtension);

            return new ImageInfo(originalUrl, smallUrl, mediumUrl, largeUrl);
        } catch (IOException e){
            throw new ImageProcessingException("Failed to process image" + e.getMessage());
        }
    }

    private void validateImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image format");
        }

        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("Invalid image file");
        }
    }

    private byte[] resizeImage(BufferedImage originalImage, Integer targetSize, String fileExtension) throws IOException {
        BufferedImage resizedImage = Scalr.resize(
                originalImage,
                Scalr.Method.QUALITY,
                Scalr.Mode.AUTOMATIC,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS
        );

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(resizedImage, fileExtension.substring(1), baos);
            return baos.toByteArray();
        } finally {
            resizedImage.flush();
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
}
