package com.warlock.controller;

import com.warlock.exceptions.AccessToResourcesException;
import com.warlock.mapper.UserMapper;
import com.warlock.model.request.UpdateUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.service.ImageProcessingService;
import com.warlock.service.ImageStorageService;
import com.warlock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User API", description = "Управление User")
public class UserController {

    @Autowired
    private final UserService userService;
    
    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final ImageProcessingService imageProcessingService;

    @Autowired
    private final ImageStorageService imageStorageService;

    @Operation(
            summary = "Получить текущего User",
            description = "Возвращает данные аутентифицированного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение данных",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Требуется аутентификация"
                    )
            }
    )
    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser() {
        var user = userService.getCurrentUser();
        return new ResponseEntity<>(
                userMapper.fromEntityToResponse(user),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получить всех User",
            description = "Возвращает список всех пользователей (кроме текущего)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение данных",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Требуется аутентификация"
                    )
            }
    )
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var currentUser = userService.getCurrentUser();
        var users = userService.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(userMapper::fromEntityToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Получить данные User",
            description = "Возвращает полную информацию о User по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение данных",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User не найден"
                    )
            }
    )
    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID User", example = "1")
            @PathVariable Long userId
    ){
        var user = userService.findById(userId);
        
        return new ResponseEntity<>(
                userMapper.fromEntityToResponse(user),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Обновить данные User",
            description = "Требует аутентификации. Позволяет обновить аватар и другие данные",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные успешно обновлены",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Требуется аутентификация"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав на редактирование"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User не найден"
                    )
            }
    )
    @PatchMapping(value = "/userId")
    @PreAuthorize("IsAuthenticated()")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID User", example = "1")
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRequest.class))
            )
            @Valid @RequestBody UpdateUserRequest request){
        var user = userService.getCurrentUser();
        var upToDateUser = userService.findById(userId);
        
        if (!user.equals(upToDateUser)){
            throw new AccessToResourcesException("Access denied");
        }
        
        var images = imageProcessingService.processImage(request.getImage(), "avatars");
        
        var requestToUpdate = userMapper.fromRequestToEntity(request,
                images.originalUrl(),
                images.smallThumbnailUrl(),
                images.mediumThumbnailUrl(),
                images.largeThumbnailUrl(),
                upToDateUser.getRole()
        );
        
        var updatedUser = userService.update(userId, requestToUpdate);
        
        return new ResponseEntity<>(userMapper.fromEntityToResponse(updatedUser), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить User",
            description = "Требует аутентификации. Удаляет аккаунт и все связанные изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Требуется аутентификация"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав на удаление"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User не найден"
                    )
            }
    )
    @DeleteMapping(value = "/{userId}")
    @PreAuthorize("IsAuthenticated()")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID User", example = "1")
            @PathVariable Long userId
    ){
        var user = userService.getCurrentUser();
        var upToDeleteUser = userService.findById(userId);

        if (!user.equals(upToDeleteUser)){
            throw new AccessToResourcesException("Access denied");
        }

        List<String> imageUrls = Arrays.asList(
                upToDeleteUser.getUrlImage(),
                upToDeleteUser.getSmallThumbnailUrl(),
                upToDeleteUser.getMediumThumbnailUrl(),
                upToDeleteUser.getLargeThumbnailUrl()
        );

        imageUrls.stream()
                .filter(Objects::nonNull)
                .forEach(url -> {
                    try {
                        imageStorageService.deleteImage(url);
                    } catch (IOException e) {
                        log.warn("Failed to delete image {}: {}", url, e.getMessage());
                    }
                });

        userService.delete(userId);

        return new ResponseEntity<>("Successful deleted user:" + userId, HttpStatus.OK);
    }
}
