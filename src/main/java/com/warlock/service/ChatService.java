package com.warlock.service;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatService {
    @NonNull
    ChatMessage save(@NonNull ChatMessage message);

    @NonNull
    ChatMessage sendMessage(@NonNull ChatMessage request);

    @NonNull
    List<ChatMessage> getConversation(@NonNull User user1, @NonNull User user2);
}
