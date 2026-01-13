package com.example.tag.domain.chat.repository;

import com.example.tag.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 방의 메시지 내역 가져오기 (오래된 순서대로)
    List<ChatMessage> findByChatRoom_IdOrderBySentAtAsc(Long roomId);
}