package com.warlock.model.shortResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Краткая информация о User")
public class UserShortResponse {
    @Schema(description = "ID User", example = "1")
    private Long id;

    @Schema(description = "Никнейм", example = "DinoLover")
    private String nickname;

    @Schema(description = "URL маленькой миниатюры аватара", example = "https://storage.com/avatars/1_thumb.jpg")
    private String smallThumbnailUrl;

    @Schema(description = "URL средней миниатюры аватара", example = "https://storage.com/avatars/1_medium.jpg")
    private String mediumThumbnailUrl;

    @Schema(description = "URL большой миниатюры аватара", example = "https://storage.com/avatars/1_large.jpg")
    private String largeThumbnailUrl;
}
