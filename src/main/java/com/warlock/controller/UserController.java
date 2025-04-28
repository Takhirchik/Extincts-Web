package com.warlock.controller;

import com.warlock.domain.TempImageUrl;
import com.warlock.exceptions.AccessToResourcesException;
import com.warlock.exceptions.ImageProcessingException;
import com.warlock.mapper.UserMapper;
import com.warlock.model.records.ImageInfo;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.request.UpdateUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.service.ImageProcessingService;
import com.warlock.service.MinioStorageService;
import com.warlock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final MinioStorageService imageStorageService;

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
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID User", example = "1")
            @PathVariable Long userId,

            @Parameter(
                    description = "Данные в формате JSON",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            )
            @RequestPart(value = "request", required = false) @Valid UpdateUserRequest request,

            @Parameter(
                    description = "Файл изображения",
                    content = @Content(mediaType = "image/*", schema = @Schema(format = "binary"))
            )
            @RequestPart(value = "image", required = false) MultipartFile imageFile,

            @Parameter(description = "URL изображения")
            @RequestPart(value = "url_image", required = false) String imageUrl
    ){

        var user = userService.getCurrentUser();
        var upToDateUser = userService.findById(userId);
        
        if (!user.equals(upToDateUser)){
            throw new AccessToResourcesException("Access denied");
        }

        var requestToUpdate = userMapper.fromRequestToEntity(request, upToDateUser.getRole());

        if (imageFile != null && !imageFile.isEmpty()){
            try {
                var images = imageProcessingService.processImage(imageFile, "avatars");
                requestToUpdate
                        .setUrlImage(images.originalUrl())
                        .setSmallThumbnailUrl(images.smallThumbnailUrl())
                        .setMediumThumbnailUrl(images.mediumThumbnailUrl())
                        .setLargeThumbnailUrl(images.largeThumbnailUrl());
            } catch (IOException e){
                throw new ImageProcessingException("Failed to process image");
            }
        } else if (imageUrl != null){
            imageProcessingService.saveUrl(imageUrl, "USER", userId);
        }

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
    @PreAuthorize("isAuthenticated()")
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
                        imageStorageService.deleteFile(url);
                    } catch (IOException e) {
                        log.warn("Failed to delete image {}: {}", url, e.getMessage());
                    }
                });

        userService.delete(userId);

        return new ResponseEntity<>("Successful deleted user:" + userId, HttpStatus.OK);
    }
}
