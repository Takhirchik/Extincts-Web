package com.warlock.factory;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.RoleResponse;
import com.warlock.model.response.UserResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserFactoryDTO {

    @Autowired
    private RoleFactoryDTO roleFactoryDTO;

    public User fromDTOToEntity(CreateUserRequest request){
        User user = new User();
        user.setNickname(request.getNickname());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        return user;
    }

    public UserResponse fromEntityToDTO(User user, RoleResponse roleResponse){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setNickname(user.getNickname());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(roleResponse);
        return userResponse;
    }

}
