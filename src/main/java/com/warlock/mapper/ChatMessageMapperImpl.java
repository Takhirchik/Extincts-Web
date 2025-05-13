package com.warlock.mapper;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import com.warlock.model.request.ChatMessageRequest;
import com.warlock.model.response.ChatMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapperImpl implements ChatMessageMapper{

    @Override
    public ChatMessage fromRequestToEntity(ChatMessageRequest request, User recipient, User sender){
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(request.getContent());
        return message;
    }

    @Override
    public ChatMessageResponse fromEntityToResponse(ChatMessage message){
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSenderName(message.getSender().getNickname());
        response.setRecipientName(message.getRecipient().getNickname());
        response.setContent(message.getContent());
        return response;
    }
}
