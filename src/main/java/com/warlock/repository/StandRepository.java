package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.Stand;

import java.util.Optional;

@Repository
public interface StandRepository extends JpaRepository<Stand, Long> {
    Optional<Stand> findByStandName (String standName);
}
