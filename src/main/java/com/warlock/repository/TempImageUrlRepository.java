package com.warlock.repository;

import com.warlock.domain.TempImageUrl;
import com.warlock.model.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TempImageUrlRepository extends JpaRepository<TempImageUrl, Long> {
    List<TempImageUrl> findByStatusAndExpiresAtAfter(ImageStatus status, LocalDateTime time);
    void deleteByExpiresAtBefore(LocalDateTime time);
}
