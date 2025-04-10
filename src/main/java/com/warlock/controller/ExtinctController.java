package com.warlock.controller;

import com.warlock.mapper.ExtinctMapper;
import com.warlock.mapper.UserMapper;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.request.UpdateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import com.warlock.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/extinct")
@Tag(name = "Extinct API", description = "Управление Extinct")
public class ExtinctController {

    @Autowired
    private final ExtinctService extinctService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final StandService standService;

    @Autowired
    private final ExtinctMapper extinctMapper;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final ImageProcessingService imageProcessingService;

    @Autowired
    private final ImageStorageService imageStorageService;

    @Operation(
            summary = "Получить Extinct по ID",
            description = "Увеличивает счетчик просмотров на 1",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение данных"),
                    @ApiResponse(responseCode = "404", description = "Extinct не найден")
            }
    )
    @GetMapping(value = "/{extinctId}")
    public ResponseEntity<ExtinctResponse> getExtinct(
            @Parameter(description = "ID Extinct", example = "1")
            @PathVariable Long extinctId
    ){
        extinctService.incrementViews(extinctId);

        var extinct = extinctService.findById(extinctId);
        var user = extinct.getCreator();

        return new ResponseEntity<>(
                extinctMapper.fromEntityToResponse(extinct, userMapper.fromEntityToShortResponse(user)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Поставить лайк Extinct",
            description = "Требует аутентификации. Увеличивает счетчик лайков на 1",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Лайк успешно поставлен"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "404", description = "Extinct не найден")
            }
    )
    @PutMapping(value = "/{extinctId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExtinctResponse> likeExtinct(
            @Parameter(description = "ID Extinct", example = "1")
            @PathVariable Long extinctId
    ){
        extinctService.incrementLikes(extinctId);

        var extinct = extinctService.findById(extinctId);
        var user = extinct.getCreator();

        return new ResponseEntity<>(
                extinctMapper.fromEntityToResponse(extinct, userMapper.fromEntityToShortResponse(user)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Создать новый Extinct",
            description = "Требует аутентификации. Загружает и обрабатывает изображения",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Extinct успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
            }
    )
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExtinctResponse> createExtinct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания вымершего вида",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateExtinctRequest.class)))
            @Valid @RequestBody CreateExtinctRequest request
    ){
        var user = userService.getCurrentUser();
        var stand = request.getStandId() == null ? null : standService.findById(request.getStandId());

        var images = imageProcessingService.processImage(request.getImage(), "extincts");

        var extinct = extinctMapper.fromCreateRequestToEntity(
                request,
                user,
                stand,
                images.originalUrl(),
                images.smallThumbnailUrl(),
                images.mediumThumbnailUrl(),
                images.largeThumbnailUrl()
        );

        var savedExtinct = extinctService.createExtinct(extinct);

        return new ResponseEntity<>(
                extinctMapper.fromEntityToResponse(savedExtinct, userMapper.fromEntityToShortResponse(user)),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Обновить Extinct",
            description = "Требует аутентификации и прав создателя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extinct успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
                    @ApiResponse(responseCode = "404", description = "Extinct не найден")
            }
    )
    @PatchMapping(value = "/{extinctId}", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExtinctResponse> updateExtinct(
            @Parameter(description = "ID Extinct", example = "1")
            @PathVariable Long extinctId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateExtinctRequest.class)))
            @Valid @RequestBody UpdateExtinctRequest request
    ){
        var user = userService.getCurrentUser();
        extinctService.isCreator(extinctId, user);

        var stand = request.getStandId() == null ? null : standService.findById(request.getStandId());

        var extinct = extinctMapper.fromUpdateRequestToEntity(request, user, stand);

        var updatedExtinct = extinctService.update(extinctId, extinct);

        return new ResponseEntity<>(
                extinctMapper.fromEntityToResponse(updatedExtinct, userMapper.fromEntityToShortResponse(user)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удалить Extinct",
            description = "Требует аутентификации и прав создателя. Удаляет все связанные изображения",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extinct успешно удален"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
                    @ApiResponse(responseCode = "404", description = "Extinct не найден")
            }
    )
    @DeleteMapping(value = "/{extinctId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteExtinct(
            @Parameter(description = "ID Extinct", example = "1")
            @PathVariable Long extinctId
    ){
        var user = userService.getCurrentUser();
        extinctService.isCreator(extinctId, user);

        var extinct = extinctService.findById(extinctId);
        List<String> imageUrls = Arrays.asList(
                extinct.getUrlImage(),
                extinct.getSmallThumbnailUrl(),
                extinct.getMediumThumbnailUrl(),
                extinct.getLargeThumbnailUrl()
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

        extinctService.delete(extinctId);
        return new ResponseEntity<>("Successful deleted extinct:" + extinctId, HttpStatus.OK);
    }

}
