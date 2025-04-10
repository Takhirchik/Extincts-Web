package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на обновление данных User")
public class UpdateUserRequest {
    @Size(min = 3, max = 30, message = "Никнейм должен быть от 3 до 30 символов")
    @Schema(description = "Новый никнейм", example = "NewNickname",
            minLength = 3, maxLength = 30)
    private String nickname;

    @Size(max = 200, message = "Биография не должна превышать 200 символов")
    @Schema(description = "Биография User", example = "Люблю динозавров",
            maxLength = 200)
    private String bio;


    @Size(min = 4, max = 20, message = "Логин должен быть от 4 до 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Логин может содержать только буквы, цифры и подчеркивания")
    @Schema(description = "Новый логин", example = "new_login123",
            minLength = 4, maxLength = 20)
    private String login;

    @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и одну цифру")
    @Schema(description = "Новый пароль", example = "NewSecurePass123",
            minLength = 8, maxLength = 30)
    private String password;

    @Email(message = "Некорректный формат email")
    @Schema(description = "Новый email", example = "new.email@example.com")
    private String email;

    @Schema(description = "Новое изображение профиля", format = "binary")
    private MultipartFile image;
}