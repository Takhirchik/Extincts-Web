package com.warlock.model.response;

import com.warlock.model.enums.ImageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Уведомление о загрузке изображения")
public class ImageUploadNotification {
    @Schema(description = "Сообщение")
    private String message;
    @Schema(description = "Статус")
    private ImageStatus status;
}
