package com.warlock.mapper;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.model.request.AuthenticateUserRequest;
import com.warlock.model.request.UpdateUserRequest;
import com.warlock.model.request.RegisterUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.model.shortResponse.UserShortResponse;

public interface UserMapper {
    User fromRequestToEntity(
            UpdateUserRequest request,
            Role role
    );
    UserResponse fromEntityToResponse(User user);
    UserShortResponse fromEntityToShortResponse(User user);
    User fromRegisterRequestToEntity(RegisterUserRequest request);
    User fromAuthenticateRequestEntity(AuthenticateUserRequest request);

}
