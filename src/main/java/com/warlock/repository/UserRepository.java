package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}