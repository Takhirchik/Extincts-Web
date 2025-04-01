package com.warlock.mapper;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.model.shortResponse.UserShortResponse;

public interface UserMapper {
    User fromRequestToEntity(
            CreateUserRequest request,
            String urlImage,
            String smThumb,
            String mdThumb,
            String lgThumb,
            Role role
    );
    UserResponse fromEntityToResponse(User user);
    UserShortResponse fromEntityToShortResponse(User user);
}
