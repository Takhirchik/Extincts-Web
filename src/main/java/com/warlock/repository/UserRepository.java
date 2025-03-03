package com.warlock.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warlock.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}