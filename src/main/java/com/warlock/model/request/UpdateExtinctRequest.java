package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на обновление Extinct")
public class UpdateExtinctRequest {

    @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
    @Schema(description = "Новое название Extinct", example = "Тираннозавр Рекс",
            minLength = 2, maxLength = 50)
    private String extinctName;


    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(description = "Новое описание Extinct", example = "Крупный хищник мелового периода",
            maxLength = 1000)
    private String description;

    @Schema(description = "ID нового стенда", example = "5")
    private Long standId;
}
