package com.warlock.mapper;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import com.warlock.model.request.CreateChatMessageRequest;
import com.warlock.model.response.ChatMessageResponse;

public interface ChatMapper {
    ChatMessage fromRequestToEntity(CreateChatMessageRequest request, User sender, User recipient);
    ChatMessageResponse fromEntityToRequest(ChatMessage message);
}
