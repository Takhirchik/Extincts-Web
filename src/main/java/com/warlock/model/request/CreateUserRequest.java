package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateUserRequest {
    private String nickname;
    private String login;
    private String password;
    private String email;
    private CreateRoleRequest role;
}