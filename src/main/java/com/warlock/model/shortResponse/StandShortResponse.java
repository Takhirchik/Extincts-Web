package com.warlock.model.shortResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Краткая информация о Stand")
public class StandShortResponse {
    @Schema(description = "ID Stand", example = "1")
    private Long id;

    @Schema(description = "Название Stand", example = "Динозавры мезозоя")
    private String standName;

    @Schema(description = "Количество просмотров", example = "250")
    private Integer views;

    @Schema(description = "Создатель Stand")
    private UserShortResponse creator;

    @Schema(description = "Обложка Stand")
    private ExtinctShortResponse coverExtinct;

    @Schema(description = "Количество видов в Stand", example = "5")
    private Integer extinctsCount;
}