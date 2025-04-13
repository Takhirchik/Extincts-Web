package com.warlock.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "Ответ с сообщением чата")
public class ChatMessageResponse {
    @Schema(description = "ID сообщения", example = "1")
    private Long id;

    @Schema(description = "ID отправителя", example = "2")
    private Long senderId;

    @Schema(description = "Имя отправителя", example = "ivan_ivanov")
    private String senderName;

    @Schema(description = "Аватар отправителя", example = "https://storage.com/avatars/1_thumb.jpg")
    private String senderSmallThumbnailUrl;

    @Schema(description = "ID получателя", example = "1")
    private Long recipientId;

    @Schema(description = "Имя получателя", example = "petr_petrov")
    private String recipientName;

    @Schema(description = "Аватар получаетеля", example = "https://storage.com/avatars/2_thumb.jpg")
    private String recipientSmallThumbnailUrl;

    @Schema(description = "Текст сообщения", example = "Привет!")
    private String content;

    @Schema(description = "Время отправки", example = "2023-05-15T10:30:00")
    private LocalDateTime timestamp;
}