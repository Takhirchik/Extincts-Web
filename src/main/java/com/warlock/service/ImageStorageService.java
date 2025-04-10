package com.warlock.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    String saveImage(byte[] imageBytes, String directory, String filename) throws IOException;
    String saveImage(MultipartFile file, String directory, String filename) throws IOException;
    void deleteImage(String url) throws IOException;
    byte[] loadImage(String url) throws IOException;
    boolean isImageExists(String url);
}
