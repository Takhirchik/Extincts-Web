package com.warlock.repository;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamp(
            User user1, User user2, User user3, User user4);
}