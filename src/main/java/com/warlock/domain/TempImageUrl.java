package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import com.warlock.model.enums.ImageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "temp_image_urls")
@Accessors(chain = true)
@Getter
@Setter
public class TempImageUrl extends BaseDomain {
    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private String targetEntity; // "user" или "extinct"
    
    @Column(nullable = false)
    private Long targetEntityId;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private Integer retryCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageStatus status = ImageStatus.PENDING;
}