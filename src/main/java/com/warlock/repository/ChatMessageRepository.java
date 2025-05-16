package com.warlock.repository;

import com.warlock.domain.ChatMessage;
import com.warlock.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.sender = :user1 AND m.recipient = :user2) " +
            "   OR (m.sender = :user2 AND m.recipient = :user1) " +
            "ORDER BY m.timestamp")
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamp(
            @Param("user1") User user1,
            @Param("user2") User user2
    );
}