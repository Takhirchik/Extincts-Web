package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Сообщение")
public class ChatMessageRequest {
    @Schema(description = "ID получателя", example = "1")
    private Long recipientId;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Schema(
            description = "Содержание сообщения",
            example = "Привет! Как дела?",
            maxLength = 1000
    )
    private String content;
}