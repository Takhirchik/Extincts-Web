package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на аутентификацию User")
public class AuthenticateUserRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов")
    @Schema(
            description = "Логин User",
            example = "john_doe",
            minLength = 3,
            maxLength = 20,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
    @Schema(
            description = "Пароль User",
            example = "securePassword123",
            minLength = 8,
            maxLength = 30,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
