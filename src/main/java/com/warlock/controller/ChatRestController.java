package com.warlock.controller;

import com.warlock.domain.User;
import com.warlock.mapper.ChatMessageMapper;
import com.warlock.model.UserActivity;
import com.warlock.model.response.ChatMessageResponse;
import com.warlock.model.response.UserResponse;
import com.warlock.service.ChatService;
import com.warlock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messenger")
@RequiredArgsConstructor
@Tag(name = "Messages API", description = "Управление чатом")
public class ChatRestController {

    @Autowired
    private final ChatService chatService;
    private final UserService userService;

    private final ChatMessageMapper chatMapper;

    @Operation(
            summary = "Получить историю сообщений",
            description = "Возвращает историю сообщений с указанным пользователем",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение истории",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessageResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Требуется аутентификация"
                    )
            }
    )
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatMessageResponse>> getChatHistory(
            @Parameter(description = "ID пользователя для истории чата")
            @PathVariable Long userId) {
        var currentUser = userService.getCurrentUser();
        var recipient = userService.findById(userId);
        return new ResponseEntity<>(
                chatService.getConversation(currentUser, recipient)
                        .stream()
                        .map(chatMapper::fromEntityToResponse)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получить всех статусов всех пользователей",
            description = "Возвращает список статусов всех пользователей (кроме текущего)",
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
    @GetMapping("/online-users")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserActivity>> getOnlineUsers()
    {
        var currentUser = userService.getCurrentUser();
        var allUsers = userService.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .toList();
        var onlineUsers = chatService.getOnlineUsersIds();

        return new ResponseEntity<>(
                allUsers
                        .stream()
                        .map(user -> new UserActivity()
                                .setId(user.getId())
                                .setNickname(user.getNickname())
                                .setOnline(onlineUsers.contains(user.getId())))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}