package com.warlock.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import lombok.NonNull;

import java.util.List;

public interface UserService {
    @NonNull
    List<User> findAll();

    @NonNull
    User findById(@NonNull Long userId);

    @NonNull
    User createUser(@NonNull User request);

    @NonNull
    User update(@NonNull Long userId, @NonNull User request);

    void delete(@NonNull Long userId);

    @NonNull
    User assignRole(@NonNull Long userId, @NonNull Role role);
}
