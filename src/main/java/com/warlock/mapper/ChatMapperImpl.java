package com.warlock.mapper;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import com.warlock.model.request.CreateChatMessageRequest;
import com.warlock.model.response.ChatMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatMapperImpl implements ChatMapper{
    @Override
    public ChatMessage fromRequestToEntity(CreateChatMessageRequest request, User sender, User recipient){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(request.getContent());
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        return chatMessage;
    }

    @Override
    public ChatMessageResponse fromEntityToRequest(ChatMessage message){
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
        chatMessageResponse.setContent(message.getContent());
        chatMessageResponse.setId(message.getId());
        chatMessageResponse.setTimestamp(message.getTimestamp());
        chatMessageResponse.setSenderId(message.getSender().getId());
        chatMessageResponse.setSenderName(message.getSender().getNickname());
        chatMessageResponse.setRecipientId(message.getRecipient().getId());
        chatMessageResponse.setRecipientName(message.getRecipient().getNickname());
        chatMessageResponse.setSenderSmallThumbnailUrl(message.getSender().getSmallThumbnailUrl());
        chatMessageResponse.setRecipientSmallThumbnailUrl(message.getRecipient().getSmallThumbnailUrl());
        return chatMessageResponse;
    }
}
