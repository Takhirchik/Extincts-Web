package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponse {
    private Long id;
    private String nickname;
    private String bio;
    private String email;
    private String url_image;
}