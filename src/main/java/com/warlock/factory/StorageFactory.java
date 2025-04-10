package com.warlock.factory;

import com.warlock.config.StorageProperties;
import com.warlock.service.ImageStorageService;
import com.warlock.service.ImageStorageServiceLocalImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageFactory {
    private final StorageProperties properties;
    private final ImageStorageServiceLocalImpl localStorage;
//  private final ImageStorageServiceS3Impl s3Storage;

    public ImageStorageService getActiveStorage() {
        return switch (properties.getType().toLowerCase()) {
            case "local" -> localStorage;
//            case "s3" -> s3Storage;
            default -> throw new IllegalStateException("Unknown storage type: " + properties.getType());
        };
    }
}