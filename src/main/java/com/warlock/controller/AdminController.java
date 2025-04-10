package com.warlock.controller;

import com.warlock.mapper.ExtinctMapper;
import com.warlock.mapper.StandsMapper;
import com.warlock.mapper.UserMapper;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import com.warlock.service.ExtinctService;
import com.warlock.service.ImageStorageService;
import com.warlock.service.StandService;
import com.warlock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/admin")
@PreAuthorize("hasRole(ADMIN)")
@Tag(name = "Admin API", description = "Управление всеми User, Stand, Extinct. Требуется роль ROLE_ADMIN")
public class AdminController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final StandService standService;

    @Autowired
    private final ExtinctService extinctService;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final ExtinctMapper extinctMapper;

    @Autowired
    private final StandsMapper standsMapper;
    
    @Autowired
    private final ImageStorageService imageStorageService;

    @Operation(
            summary = "Найти все User",
            description = "Требует прав админа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping(value = "/users")
    public ResponseEntity<List<UserShortResponse>> getAllUsers(){
        return new ResponseEntity<>(
                userService.findAll()
                        .stream()
                        .map(userMapper::fromEntityToShortResponse)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Найти все Extinct",
            description = "Требует прав админа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping(value = "/extincts")
    public ResponseEntity<List<ExtinctShortResponse>> getAllExtincts(){
        return new ResponseEntity<>(
                extinctService.findAll()
                        .stream()
                        .map(extinct -> extinctMapper.fromEntityToShortResponse(
                                extinct,
                                userMapper.fromEntityToShortResponse(extinct.getCreator())
                                )
                        )
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Найти все Stand",
            description = "Требует прав админа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @GetMapping(value = "/stands")
    public ResponseEntity<List<StandShortResponse>> getAllStands(){
        return new ResponseEntity<>(
                standService.findAll()
                        .stream()
                        .map(stand -> standsMapper.fromEntityToShortResponse(
                                stand,
                                userMapper.fromEntityToShortResponse(stand.getCreator()),
                                extinctMapper.fromEntityToShortResponse(
                                        stand.getExtincts().getFirst(),
                                        userMapper.fromEntityToShortResponse(stand.getExtincts().getFirst().getCreator())
                                        )
                                )
                        )
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Назначить всем указанным User роль ROLE_ADMIN",
            description = "Требует прав админа. Назначает роль по ID через query-параметр",
            parameters = {
                    @Parameter(name = "query", description = "ID User через пробел", example = "1+2+3", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роли успешно назначены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PatchMapping(value = "/users")
    public ResponseEntity<String> assignRoles(@RequestParam(required = true) String query){
        var admin = userService.getCurrentUser();
        Arrays.stream(query.split("\\s+"))
                .map(Long::parseLong)
                .forEach(id -> {
                    log.info("Admin {} is trying to assign ROLE_ADMIN to User {}", admin.getNickname(), id);
                    try{
                        userService.assignRole(id);
                        log.info("Admin {} is successful assigned ROLE_ADMIN to User {}", admin.getNickname(), id);
                    } catch (Exception e){
                        log.warn("Failed to assign ROLE_ADMIN to User {}", id);
                    }
                });
        return new ResponseEntity<>("Successful assigned users", HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить всех указанных User",
            description = "Требует прав админа. Удаляет по ID через query-параметр",
            parameters = {
                    @Parameter(name = "query", description = "ID User через пробел", example = "1+2+3", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное удаление"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @DeleteMapping(value = "/users")
    public ResponseEntity<String> deleteUsers(@RequestParam(required = true) String query){
        var admin = userService.getCurrentUser();
        Arrays.stream(query.split("\\s+"))
                .map(Long::parseLong)
                .forEach(id -> {
                    log.info("Admin {} is trying to delete User {}", admin.getNickname(), id);
                    try{
                        var user = userService.findById(id);
                        List<String> imageUrls = Arrays.asList(
                                user.getUrlImage(),
                                user.getSmallThumbnailUrl(),
                                user.getMediumThumbnailUrl(),
                                user.getLargeThumbnailUrl()
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
                        userService.delete(id);
                        log.info("Admin {} is successful deleted User {}", admin.getNickname(), id);
                    } catch (Exception e){
                        log.warn("Failed to delete User {}", id);
                    }
                });
        return new ResponseEntity<>("Successful deleted users", HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить все указанные Extinct",
            description = "Требует прав админа. Удаляет по ID через query-параметр",
            parameters = {
                    @Parameter(name = "query", description = "ID Extinct через пробел", example = "1+2+3", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное удаление"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @DeleteMapping(value = "/extincts")
    public ResponseEntity<String> deleteExtincts(@RequestParam(required = true) String query){
        var admin = userService.getCurrentUser();
        Arrays.stream(query.split("\\s+"))
                .map(Long::parseLong)
                .forEach(id -> {
                    log.info("Admin {} is trying to delete Extinct {}", admin.getNickname(), id);
                    try{
                        var extinct = extinctService.findById(id);
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
                        extinctService.delete(id);
                        log.info("Admin {} is successful deleted extinct {}", admin.getNickname(), id);
                    } catch (Exception e){
                        log.warn("Failed to delete extinct {}", id);
                    }
                });
        return new ResponseEntity<>("Successful deleted extinct", HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить все указанные Stand",
            description = "Требует прав админа. Удаляет по ID через query-параметр",
            parameters = {
                    @Parameter(name = "query", description = "ID Stand через пробел", example = "1+2+3", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное удаление"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @DeleteMapping(value = "/stands")
    public ResponseEntity<String> deleteStands(@RequestParam(required = true) String query){
        var admin = userService.getCurrentUser();
        Arrays.stream(query.split("\\s+"))
                .map(Long::parseLong)
                .forEach(id -> {
                    log.info("Admin {} is trying to delete Stand {}", admin.getNickname(), id);
                    try{
                        standService.delete(id);
                        log.info("Admin {} is successful deleted stand {}", admin.getNickname(), id);
                    } catch (Exception e){
                        log.warn("Failed to delete stand {}", id);
                    }
                });
        return new ResponseEntity<>("Successful deleted stands", HttpStatus.OK);
    }
}
