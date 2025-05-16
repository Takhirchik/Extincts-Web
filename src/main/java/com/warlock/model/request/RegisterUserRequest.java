package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на регистрацию User")
public class RegisterUserRequest {
    @NotBlank(message = "Никнейм не может быть пустым")
    @Size(min = 3, max = 30, message = "Никнейм должен быть от 3 до 30 символов")
    @Schema(description = "Никнейм User", example = "JohnDoe",
            minLength = 3, maxLength = 30, requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 20, message = "Логин должен быть от 4 до 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Логин может содержать только буквы, цифры и подчеркивания")
    @Schema(description = "Логин для входа", example = "john_doe123",
            minLength = 4, maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и одну цифру")
    @Schema(description = "Пароль", example = "SecurePass123",
            minLength = 8, maxLength = 30, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Schema(description = "Подтверждение пароля", example = "SecurePass123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmPassword;
    
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email User", example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
