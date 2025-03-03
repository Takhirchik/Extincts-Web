package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.ExtinctImage;

import java.util.Optional;
import java.util.List;

@Repository
public interface ExtinctImageRepository extends JpaRepository<ExtinctImage, Long> {
}
