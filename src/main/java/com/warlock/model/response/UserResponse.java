package com.warlock.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Полная информация о User")
public class UserResponse {
    @Schema(description = "ID User", example = "1")
    private Long id;

    @Schema(description = "Никнейм", example = "DinoLover")
    private String nickname;

    @Schema(description = "Биография", example = "Энтузиаст палеонтологии")
    private String bio;

    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @Schema(description = "URL аватара", example = "https://storage.com/avatars/1.jpg")
    private String urlImage;
}