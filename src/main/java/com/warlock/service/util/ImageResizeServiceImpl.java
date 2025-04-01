package com.warlock.service.util;

import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageResizeServiceImpl implements ImageResizeService {

    @Override
    public BufferedImage resizeImage(BufferedImage original, Integer width, Integer height){
        return Scalr.resize(original, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, width, height);
    }

    @Override
    public Map<String, BufferedImage> generateAllThumbnails(BufferedImage original){
        Map<String, BufferedImage> thumbnailMap = new HashMap<>();
        thumbnailMap.put("small", resizeImage(original, 150, 150));
        thumbnailMap.put("medium", resizeImage(original, 300, 300));
        thumbnailMap.put("large", resizeImage(original, 600, 600));
        return thumbnailMap;
    }
}
