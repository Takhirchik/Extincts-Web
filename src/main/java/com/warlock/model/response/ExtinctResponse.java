package com.warlock.model.response;

import com.warlock.model.shortResponse.UserShortResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(description = "Полная информация о Extinct")
public class ExtinctResponse {
    @Schema(description = "ID Extinct", example = "1")
    private Long id;
    @Schema(description = "Название Extinct", example = "Тираннозавр")
    private String extinctName;
    @Schema(description = "Описание Extinct", example = "Крупный хищный динозавр")
    private String description;
    @Schema(description = "Количество просмотров", example = "150")
    private Integer views;
    @Schema(description = "Количество лайков", example = "42")
    private Integer likes;
    @Schema(description = "Дата создания", example = "2023-01-15")
    private LocalDate createdAt;
    @Schema(description = "Создатель Extinct")
    private UserShortResponse creator;
    @Schema(description = "URL основного изображения", example = "https://storage.com/extincts/1.jpg")
    private String url_img;
}
