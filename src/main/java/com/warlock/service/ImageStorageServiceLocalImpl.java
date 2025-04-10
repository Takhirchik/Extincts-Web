package com.warlock.service;

import com.warlock.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class ImageStorageServiceLocalImpl implements ImageStorageService{
    private final StorageProperties storageProperties;

    @Override
    public String saveImage(byte[] imageBytes, String directory, String filename) throws IOException {
        Path uploadPath = Paths.get(storageProperties.getLocal().getDirectory(), directory);
        ensureDirectoryExists(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, imageBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return formatFileUrl(directory, filename);
    }

    @Override
    public String saveImage(MultipartFile file, String directory, String filename) throws IOException{
        Path uploadPath = Paths.get(storageProperties.getLocal().getDirectory(), directory);
        ensureDirectoryExists(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return formatFileUrl(directory, filename);
    }

    @Override
    public void deleteImage(String url) throws IOException{
        Path filePath = extractFilePathFromUrl(url);
        Files.deleteIfExists(filePath);
    }

    @Override
    public byte[] loadImage(String url) throws IOException {
        Path filePath = extractFilePathFromUrl(url);
        return Files.readAllBytes(filePath);
    }

    @Override
    public boolean isImageExists(String url) {
        try {
            Path filePath = extractFilePathFromUrl(url);
            return Files.exists(filePath);
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureDirectoryExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private String formatFileUrl(String directory, String filename) {
        return storageProperties.getBaseUrl() + directory + "/" + filename;
    }

    private Path extractFilePathFromUrl(String url) {
        String relativePath = url.replace(storageProperties.getBaseUrl(), "");
        return Paths.get(storageProperties.getLocal().getDirectory(), relativePath);
    }
}
