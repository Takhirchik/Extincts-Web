package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание Stand")
public class CreateStandRequest {
    @NotBlank(message = "Название Stand не может быть пустым")
    @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
    @Schema(
            description = "Название Stand",
            example = "Динозавры мезозоя",
            minLength = 2,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String standName;
    
    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(
            description = "Описание Stand",
            example = "Коллекция древних рептилий мезозойской эры",
            maxLength = 1000
    )
    private String description;
}
