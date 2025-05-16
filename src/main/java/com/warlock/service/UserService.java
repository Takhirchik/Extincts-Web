package com.warlock.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import lombok.NonNull;
import org.checkerframework.checker.units.qual.N;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    @NonNull
    List<User> findAll();

    @NonNull
    User findById(@NonNull Long userId);

    @NonNull
    User save(@NonNull User user);

    @NonNull
    User create(@NonNull User user);

    @NonNull
    User update(@NonNull Long userId, @NonNull User request);

    void delete(@NonNull Long userId);

    @NonNull
    User assignRole(@NonNull Long userId);

    @NonNull
    User getCurrentUser();

    @NonNull
    User getByLogin(@NonNull String login);

    UserDetailsService userDetailsService();

    @NonNull
    User updateUserImage(
            @NonNull Long userId,
            @NonNull String originalUrl,
            @NonNull String smallThumbnailUrl,
            @NonNull String mediumThumbnailUrl,
            @NonNull String largeThumbnailUrl
    );
}
