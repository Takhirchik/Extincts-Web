package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponse {
    private Long id;
    private String nickname;
    private String login;
    private String password;
    private String email;
    private RoleResponse role;
}