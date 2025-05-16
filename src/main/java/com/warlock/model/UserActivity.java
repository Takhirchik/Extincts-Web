package com.warlock.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Уведомления об активности пользователя")
public class UserActivity {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    @Schema(description = "Никнейм пользователя", example = "JohnDoe")
    private String nickname;
    @Schema(description = "Метка в онлайне ли пользователь")
    private boolean online;
}