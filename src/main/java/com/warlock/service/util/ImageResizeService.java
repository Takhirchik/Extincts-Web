package com.warlock.service.util;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface ImageResizeService {
    BufferedImage resizeImage(BufferedImage original, Integer width, Integer height);
    Map<String, BufferedImage> generateAllThumbnails(BufferedImage original);
}
