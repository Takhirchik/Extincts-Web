package com.warlock.service;

import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.UserResponse;
import lombok.NonNull;

import java.util.List;

public interface UserService {
    @NonNull
    List<UserResponse> findAll();

    @NonNull
    UserResponse findById(@NonNull Long userId);

    @NonNull
    UserResponse createUser(@NonNull CreateUserRequest request);

    @NonNull
    UserResponse update(@NonNull Long userId, @NonNull CreateUserRequest request);

    void delete(@NonNull Long userId);
}
