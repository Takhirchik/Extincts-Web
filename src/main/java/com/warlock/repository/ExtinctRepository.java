package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.Extinct;

import java.util.Optional;
import java.util.List;

@Repository
public interface ExtinctRepository extends JpaRepository<Extinct, Long> {
}
