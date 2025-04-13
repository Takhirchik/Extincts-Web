package com.warlock.service;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import com.warlock.repository.ChatMessageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;


    /**
     * Сохранение сообщения
     *
     * @param message сообщение
     * @return сохранённое сообщение
     */
    @Override
//    @Transactional
    public @NonNull ChatMessage save(@NonNull ChatMessage message){
        return chatMessageRepository.save(message);
    }


    /**
     * Создать сообщение
     *
     * @param request запрос на создание сообщения
     * @return созданное сообщение
     */
    @Override
    public @NonNull ChatMessage sendMessage(@NonNull ChatMessage request) {
        request.setTimestamp(LocalDateTime.now());

        return save(request);
    }

    /**
     * Найти историю сообщений двух пользователей
     *
     * @param user1 1 пользователь
     * @param user2 2 пользователь
     * @return историю сообщений двух пользователей
     */
    @Override
    public @NonNull List<ChatMessage> getConversation(@NonNull User user1, @NonNull User user2) {
        return chatMessageRepository
                .findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamp(
                        user1, user2, user2, user1
                );
    }
}
