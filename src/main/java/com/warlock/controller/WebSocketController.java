package com.warlock.controller;

import com.warlock.mapper.ChatMapper;
import com.warlock.model.request.CreateChatMessageRequest;
import com.warlock.model.response.ChatMessageResponse;
import com.warlock.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserService userService;

    private final ChatMapper chatMapper;

    @MessageMapping("/process-message")
    public ChatMessageResponse sendMessage(@Payload CreateChatMessageRequest message, Principal principal) {
        var sender = userService.getByLogin(principal.getName());
        var recipient = userService.findById(message.getRecipientId());

        var messageToSend = chatMapper.fromRequestToEntity(message, sender, recipient);

        var savedMessage = chatService.sendMessage(messageToSend);
        
        var response = chatMapper.fromEntityToRequest(savedMessage);

        messagingTemplate.convertAndSend("/chat/" + response.getRecipientName());

        return response;
    }
}