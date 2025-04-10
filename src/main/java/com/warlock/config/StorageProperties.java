package com.warlock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    private String baseUrl;
    private String type;
    private Local local;
    private S3 s3;

    @Getter
    @Setter
    public static class Local{
        private String directory;
    }

    @Getter
    @Setter
    public static class S3{
        private String bucketName;
        private String accessKey;
        private String secretKey;
        private String region;
    }
}
