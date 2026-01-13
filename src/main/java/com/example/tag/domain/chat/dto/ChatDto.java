package com.example.tag.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChatDto {

    // 채팅방 생성 요청
    @Getter
    @NoArgsConstructor
    public static class RoomRequest {
        private Long itemId; // 상품 ID만 보내면 판매자는 자동으로 조회됨
    }

    // 채팅방 목록 조회 응답
    @Getter
    @Builder
    @AllArgsConstructor
    public static class RoomResponse {
        private Long roomId;
        private String partnerName; // 상대방 이름 (내가 구매자면 판매자 이름, 반대면 구매자 이름)
        private String lastMessage; // 마지막 대화 내용
        private LocalDateTime lastMessageTime; // 마지막 대화 시간
        private String itemTitle;   // 상품명
        private String itemImageUrl;// 상품 이미지
    }

    // 메시지 전송 요청 (WebSocket or API)
    @Getter
    @NoArgsConstructor
    public static class MessageRequest {
        private Long roomId;
        private String message;
    }

    // 개별 메시지 응답
    @Getter
    @Builder
    @AllArgsConstructor
    public static class MessageResponse {
        private Long messageId;
        private Long senderId;
        private String senderName;
        private String message;
        private LocalDateTime sentAt;
        private boolean isRead;
    }
}