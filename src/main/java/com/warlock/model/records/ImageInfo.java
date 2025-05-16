package com.warlock.model.records;

public record ImageInfo(
        String originalUrl,
        String smallThumbnailUrl,
        String mediumThumbnailUrl,
        String largeThumbnailUrl
) {}
