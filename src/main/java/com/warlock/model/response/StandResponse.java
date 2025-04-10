package com.warlock.model.response;

import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "Полная информация о Stand")
public class StandResponse {
    @Schema(description = "ID Stand", example = "1")
    private Long id;

    @Schema(description = "Название Stand", example = "Динозавры мезозоя")
    private String standName;

    @Schema(description = "Описание Stand", example = "Коллекция древних рептилий")
    private String description;

    @Schema(description = "Количество просмотров", example = "250")
    private Integer views;

    @Schema(description = "Дата создания", example = "2023-02-20")
    private LocalDate createdAt;

    @Schema(description = "Создатель Stand")
    private UserShortResponse creator;

    @Schema(description = "Обложка Stand (первый вымерший вид)")
    private ExtinctShortResponse coverExtinct;

    @Schema(description = "Список вымерших видов в Stand")
    private List<ExtinctShortResponse> extincts;
}
