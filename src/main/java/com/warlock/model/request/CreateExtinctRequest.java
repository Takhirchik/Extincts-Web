package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на создание Extinct")
public class CreateExtinctRequest {
    @NotBlank(message = "Название Extinct не может быть пустым")
    @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
    @Schema(
            description = "Название Extinct",
            example = "Тираннозавр",
            minLength = 2,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String extinctName;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(
            description = "Описание Extinct",
            example = "Крупный хищный динозавр мелового периода",
            maxLength = 1000
    )
    private String description;

    @Schema(description = "ID Stand, к которому привязан Extinct", example = "1")
    private Long standId;
}
