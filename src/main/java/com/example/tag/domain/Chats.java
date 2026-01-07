package com.example.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId; // 메시지 번호 [cite: 12]

    @Column(nullable = false, length = 50)
    private String roomId; // 방 ID [cite: 12]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender; // 보낸 사람 [cite: 12]

    @Column(nullable = false, length = 10)
    private String msgType = "TEXT"; // 타입 (Default: TEXT) [cite: 12]

    @Column(columnDefinition = "TEXT")
    private String content; // 내용 [cite: 12]

    @Column(nullable = false)
    private Boolean isRead = false; // 읽음 여부 (Default: 0) [cite: 12]

    @Column(nullable = false)
    private LocalDateTime sentAt; // 전송 시간 [cite: 12]
}