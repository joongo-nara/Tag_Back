package com.example.tag.domain.chat.entity;

import com.example.tag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    // 메시지 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean isRead; // 읽음 확인

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sentAt;

    // ChatMessage 클래스 내부에 추가
    public static ChatMessage create(ChatRoom room, User sender, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.chatRoom = room;
        chatMessage.sender = sender;
        chatMessage.message = message;
        chatMessage.isRead = false;
        chatMessage.sentAt = LocalDateTime.now();
        return chatMessage;
    }
}