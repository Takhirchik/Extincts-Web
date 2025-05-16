package com.warlock.domain;

import com.warlock.domain.common.BaseDomain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "chat_messages")
public class ChatMessage extends BaseDomain {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage chatMessage = (ChatMessage) o;
        return sender.equals(chatMessage.sender) && recipient.equals(chatMessage.recipient) &&
                content.equals(chatMessage.content) && timestamp.equals(chatMessage.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient, content, timestamp);
    }

    @Override
    public String toString(){
        return "ChatMessage{" +
                "sender=" + this.sender +
                ", recipient=" + this.recipient +
                ", content=" + this.content +
                ", timestamp=" + this.timestamp +
                "}";
    }
}