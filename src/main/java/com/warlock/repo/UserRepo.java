package com.warlock.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warlock.model.User;

public interface UserRepo extends JpaRepository<User, Long> {}