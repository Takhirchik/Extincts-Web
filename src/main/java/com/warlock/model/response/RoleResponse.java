package com.warlock.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Информация о роли пользователя")
public class RoleResponse {
    @Schema(description = "ID роли", example = "1")
    private Long id;
    @Schema(description = "Название роли", example = "ROLE_ADMIN")
    private String name;
}
