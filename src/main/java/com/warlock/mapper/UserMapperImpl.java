package com.warlock.mapper;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.model.request.AuthenticateUserRequest;
import com.warlock.model.request.UpdateUserRequest;
import com.warlock.model.request.RegisterUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper{
    @Override
    @Deprecated
    public User fromRequestToEntity(
            UpdateUserRequest request,
            String urlImage,
            String smThumb,
            String mdThumb,
            String lgThumb,
            Role role
    ){
        User user = new User();
        user.setNickname(request.getNickname());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
        user.setRole(role);
        user.setUrlImage(urlImage);
        user.setSmallThumbnailUrl(smThumb);
        user.setMediumThumbnailUrl(mdThumb);
        user.setLargeThumbnailUrl(lgThumb);
        return user;
    }

    @Override
    public UserResponse fromEntityToResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setBio(user.getBio());
        response.setEmail(user.getEmail());
        response.setUrlImage(user.getUrlImage());
        return response;
    }

    @Override
    public UserShortResponse fromEntityToShortResponse(User user){
        UserShortResponse response = new UserShortResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setSmallThumbnailUrl(user.getSmallThumbnailUrl());
        response.setMediumThumbnailUrl(user.getMediumThumbnailUrl());
        response.setLargeThumbnailUrl(user.getLargeThumbnailUrl());
        return response;
    }

    @Override
    public User fromRegisterRequestToEntity(RegisterUserRequest request){
        User user = new User();
        user.setNickname(request.getNickname());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        return user;
    }

    @Override
    public User fromAuthenticateRequestEntity(AuthenticateUserRequest request){
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        return user;
    }

}
