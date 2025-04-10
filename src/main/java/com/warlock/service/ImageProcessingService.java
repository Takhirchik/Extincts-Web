package com.warlock.service;

import com.warlock.model.records.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ImageProcessingService {
    ImageInfo processImage(MultipartFile file, String directory);
}
