package com.warlock.controller;

import com.warlock.mapper.ExtinctMapper;
import com.warlock.mapper.StandsMapper;
import com.warlock.mapper.UserMapper;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandResponse;
import com.warlock.service.StandService;
import com.warlock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/stand")
@RestController
@RequiredArgsConstructor
@Tag(name = "Stand API", description = "Управление Stand (коллекциями Extinct)")
public class StandController {
    @Autowired
    private final StandService standService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final StandsMapper standsMapper;

    @Autowired
    private final ExtinctMapper extinctMapper;

    @Autowired
    private final UserMapper userMapper;

    @Operation(
            summary = "Получить Stand по ID",
            description = "Возвращает полную информацию о Stand и его Extinct",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение данных",
                            content = @Content(schema = @Schema(implementation = StandResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Stand не найден")
            }
    )
    @GetMapping(value = "/{standId}")
    public ResponseEntity<StandResponse> getStand(
            @Parameter(description = "ID Stand", example = "1")
            @PathVariable Long standId
    ) {
        var stand = standService.findById(standId);

        standService.incrementViews(standId);

        var userShortResponse = userMapper.fromEntityToShortResponse(stand.getCreator());
        var extincts = standService.findAllExtincts(stand);

        var extinctsShortResponse = extincts
                .stream()
                .map(extinct -> {
                    var creator = userMapper.fromEntityToShortResponse(extinct.getCreator());
                    return extinctMapper.fromEntityToShortResponse(extinct, creator);
                })
                .toList();
        var coverImage = extincts.isEmpty() ? null :
                extinctMapper.fromEntityToShortResponse(
                        extincts.getFirst(),
                        userMapper.fromEntityToShortResponse(extincts.getFirst().getCreator())
                );

        return new ResponseEntity<>(
                standsMapper.fromEntityToResponse(stand, userShortResponse, coverImage, extinctsShortResponse),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Создать новый Stand",
            description = "Требует аутентификации",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Stand успешно создан",
                            content = @Content(schema = @Schema(implementation = StandResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
            }
    )
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StandResponse> createStand(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания Stand",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateStandRequest.class)))
            @Valid @RequestBody CreateStandRequest request
    ){
        var user = userService.getCurrentUser();
        var stand = standsMapper.fromRequestToEntity(request, user);

        var standSaved = standService.createStand(stand);
        var userShortResponse = userMapper.fromEntityToShortResponse(user);

        return new ResponseEntity<>(
                standsMapper.fromEntityToResponse(standSaved, userShortResponse, null, Collections.emptyList()),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Обновить Stand",
            description = "Требует аутентификации и прав создателя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Stand успешно обновлен",
                            content = @Content(schema = @Schema(implementation = StandResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
                    @ApiResponse(responseCode = "404", description = "Stand не найден")
            }
    )
    @PatchMapping(value = "/{standId}/change", consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StandResponse> updateStand(
            @Parameter(description = "ID Stand", example = "1")
            @PathVariable Long standId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления Stand",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateStandRequest.class)))
            @Valid @RequestBody CreateStandRequest request){
        var user = userService.getCurrentUser();
        standService.isCreator(standId, user);

        var stand = standsMapper.fromRequestToEntity(request, user);

        var updatedStand = standService.update(standId, stand);

        var userShortResponse = userMapper.fromEntityToShortResponse(updatedStand.getCreator());

        var extincts = standService.findAllExtincts(updatedStand);
        var extinctsShortResponse = extincts
                .stream()
                .map(extinct -> {
                    var creator = userMapper.fromEntityToShortResponse(extinct.getCreator());
                    return extinctMapper.fromEntityToShortResponse(extinct, creator);
                })
                .toList();
        var firstExtinct = extincts.getFirst();
        var coverImage = extinctMapper.fromEntityToShortResponse(
                firstExtinct,
                userMapper.fromEntityToShortResponse(firstExtinct.getCreator())
        );

        return new ResponseEntity<>(
                standsMapper.fromEntityToResponse(updatedStand, userShortResponse, coverImage, extinctsShortResponse),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Добавить Extinct в Stand",
            description = "Требует аутентификации и прав создателя Stand",
            parameters = {
                    @Parameter(name = "extinctId", description = "ID добавляемого Extinct", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extinct успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
                    @ApiResponse(responseCode = "404", description = "Stand или Extinct не найден")
            }
    )
    @PatchMapping(value = "/{standId}/add_extinct")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addExtinct(
            @Parameter(description = "ID Stand", example = "1")
            @PathVariable Long standId,
            @RequestParam(required = true) Long extinctId
    ){
        var user = userService.getCurrentUser();
        standService.isCreator(standId, user);
        standService.addExtinct(standId, extinctId);
        return new ResponseEntity<>(
                "Successful adding extinct:" + extinctId + " to stand:" + standId,
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удалить Extinct из Stand",
            description = "Требует аутентификации и прав создателя Stand",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extinct успешно удален"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на редактирование"),
                    @ApiResponse(responseCode = "404", description = "Stand или Extinct не найден")
            }
    )
    @PatchMapping(value = "/{standId}/extinct/{extinctId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteExtinct(
            @Parameter(description = "ID Stand", example = "1")
            @PathVariable Long standId,
            @Parameter(description = "ID Extinct", example = "1")
            @PathVariable Long extinctId
    ){
        var user = userService.getCurrentUser();
        standService.isCreator(standId, user);
        standService.deleteExtinct(standId, extinctId);
        return new ResponseEntity<>(
                "Successful deleting extinct:" + extinctId + " from stand:" + standId,
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удалить Stand",
            description = "Требует аутентификации и прав создателя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stand успешно удален"),
                    @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
                    @ApiResponse(responseCode = "404", description = "Stand не найден")
            }
    )
    @DeleteMapping(value = "/{standId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteStand(
            @Parameter(description = "ID Stand", example = "1")
            @PathVariable Long standId
    ){
        var user = userService.getCurrentUser();
        standService.isCreator(standId, user);
        standService.delete(standId);
        return new ResponseEntity<>("Successful deleted stand:" + standId, HttpStatus.OK);
    }
}
