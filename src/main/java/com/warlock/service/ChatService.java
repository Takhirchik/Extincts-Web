package com.warlock.service;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

public interface ChatService {
    @NonNull
    ChatMessage save(@NonNull ChatMessage message);

    @NonNull
    ChatMessage sendMessage(@NonNull ChatMessage request);

    @NonNull
    List<ChatMessage> getConversation(@NonNull User user1, @NonNull User user2);

    void userConnected(@NonNull Long userId);

    void userDisconnected(@NonNull Long userId);

    Set<Long> getOnlineUsersIds();
}