package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.Stand;

import java.util.Optional;
import java.util.List;

@Repository
public interface StandRepository extends JpaRepository<Stand, Long> {
    List<Stand> findByStandCategoryId (Long standCategoryId);
}
