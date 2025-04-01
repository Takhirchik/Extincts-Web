package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
public class CreateUserRequest {
    private String nickname;
    private String bio;
    private String login;
    private String password;
    private String email;
    private MultipartFile image;
}