package com.warlock.controller;

import com.warlock.mapper.ChatMessageMapper;
import com.warlock.model.UserActivity;
import com.warlock.model.request.ChatMessageRequest;
import com.warlock.service.ChatService;
import com.warlock.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatMessageService;
    private final UserService userService;
    private final ChatMessageMapper chatMessageMapper;

    // Отправка сообщения
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageRequest message, Principal principal) {
        var sender = userService.getByLogin(principal.getName());
        var recipient = userService.findById(message.getRecipientId());

        var messageEntity = chatMessageMapper.fromRequestToEntity(message, recipient, sender);

        // Сохранение сообщения в БД
        var savedMessage = chatMessageService.sendMessage(messageEntity);

        // Отправка получателю
        messagingTemplate.convertAndSendToUser(
                recipient.getId().toString(),
                "/queue/messages",
                chatMessageMapper.fromEntityToResponse(savedMessage)
        );
        log.info("{} отправил сообщение {}", sender.getNickname(), recipient.getNickname());
    }

    // Уведомления о подключении/отключении
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String login = headers.getUser().getName();

        var user = userService.getByLogin(login);

        messagingTemplate.convertAndSend(
                "/topic/activity",
                new UserActivity().setNickname(user.getNickname()).setOnline(true)
        );
        log.info("Клиент подключился к сессии: sessionId={}, headers={}", headers.getSessionId(), headers);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String login = headers.getUser().getName();

        var user = userService.getByLogin(login);

        messagingTemplate.convertAndSend(
                "/topic/activity",
                new UserActivity().setNickname(user.getNickname()).setOnline(false)
        );
        log.info("Клиент отключился от сессии: sessionId={}", headers.getSessionId());
    }
}
