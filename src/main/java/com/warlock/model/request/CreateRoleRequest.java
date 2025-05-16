package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание роли")
public class CreateRoleRequest {

    @NotBlank(message = "Название роли не может быть пустым")
    @Size(min = 3, max = 20, message = "Название роли должно быть от 3 до 20 символов")
    @Pattern(regexp = "^[A-Z_]+$", message = "Название роли должно содержать только заглавные буквы и подчеркивания")
    @Schema(
            description = "Название роли (только заглавные буквы и подчеркивания)",
            example = "ROLE_ADMIN",
            pattern = "^[A-Z_]+$",
            minLength = 3,
            maxLength = 20,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}