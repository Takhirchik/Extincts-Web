package com.warlock.mapper;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import com.warlock.model.request.ChatMessageRequest;
import com.warlock.model.response.ChatMessageResponse;

public interface ChatMessageMapper {
    ChatMessage fromRequestToEntity(ChatMessageRequest request, User recipient, User sender);
    ChatMessageResponse fromEntityToResponse(ChatMessage message);

}
