package com.warlock.model.shortResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Краткая информация о Extinct")
public class ExtinctShortResponse {
    @Schema(description = "ID Extinct", example = "1")
    private Long id;

    @Schema(description = "Название Extinct", example = "Тираннозавр")
    private String extinctName;

    @Schema(description = "Количество просмотров", example = "150")
    private Integer views;

    @Schema(description = "Количество лайков", example = "42")
    private Integer likes;

    @Schema(description = "Создатель Extinct")
    private UserShortResponse creator;

    @Schema(description = "URL маленькой миниатюры", example = "https://storage.com/extincts/1_thumb.jpg")
    private String smallThumbnailUrl;

    @Schema(description = "URL средней миниатюры", example = "https://storage.com/extincts/1_medium.jpg")
    private String mediumThumbnailUrl;

    @Schema(description = "URL большой миниатюры", example = "https://storage.com/extincts/1_large.jpg")
    private String largeThumbnailUrl;
}
