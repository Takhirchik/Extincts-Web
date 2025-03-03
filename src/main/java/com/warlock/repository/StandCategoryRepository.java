package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.StandCategory;

import java.util.Optional;

@Repository
public interface StandCategoryRepository extends JpaRepository<StandCategory, Long>{
    Optional<StandCategory> findByName(String standName);
}
