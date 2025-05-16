package com.warlock.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Сообщение")
public class ChatMessageRequest {
    @Schema(description = "ID получателя сообщения", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID получателя обязателен")
    @Positive(message = "ID получателя должен быть положительным числом")
    private Long recipientId;

    @Schema(description = "Текст сообщения", example = "Привет!",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 2000)
    @NotBlank(message = "Текст сообщения обязателен")
    @Size(min = 1, max = 2000, message = "Сообщение должно содержать от 1 до 2000 символов")
    private String content;
}