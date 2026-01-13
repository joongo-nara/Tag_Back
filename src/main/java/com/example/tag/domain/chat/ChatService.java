package com.example.tag.domain.chat;

import com.example.tag.domain.chat.dto.ChatDto;
import com.example.tag.domain.chat.entity.ChatMessage;
import com.example.tag.domain.chat.entity.ChatRoom;
import com.example.tag.domain.chat.repository.ChatMessageRepository;
import com.example.tag.domain.chat.repository.ChatRoomRepository;
import com.example.tag.domain.product.entity.Product;
import com.example.tag.domain.product.repository.ProductRepository;
import com.example.tag.domain.user.entity.User;
import com.example.tag.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 1. 채팅방 생성 (이미 있으면 기존 방 ID 반환)
    @Transactional
    public Long createChatRoom(Long userId, Long productId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // 자신이 올린 상품에는 채팅 불가
        if (product.getSubmission().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("자신의 상품에는 채팅을 걸 수 없습니다.");
        }

        // 이미 생성된 방이 있는지 확인
        return chatRoomRepository.findByProduct_ProductIdAndBuyer_UserId(productId, userId)
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    ChatRoom room = ChatRoom.createRoom(product, buyer, product.getSubmission().getUser()); // 판매자는 product.getUser()
                    chatRoomRepository.save(room);
                    return room.getId();
                });
    }

    // 2. 내 채팅방 목록 조회
    public List<ChatDto.RoomResponse> getMyChatRooms(Long userId) {
        // 내가 구매자이거나 판매자인 방을 모두 찾음
        List<ChatRoom> rooms = chatRoomRepository.findByBuyer_UserIdOrSeller_UserIdOrderByCreatedAtDesc(userId, userId);

        return rooms.stream().map(room -> {
            // 상대방 이름 찾기 (내가 구매자면 -> 판매자가 상대방, 반대면 구매자가 상대방)
            User partner = room.getBuyer().getUserId().equals(userId) ? room.getSeller() : room.getBuyer();

            // 마지막 메시지 찾기 (없으면 공백)
            // 성능 최적화를 위해선 쿼리를 따로 짜는 게 좋지만, 일단 간단하게 구현
            List<ChatMessage> messages = chatMessageRepository.findByChatRoom_IdOrderBySentAtAsc(room.getId());
            String lastMessage = messages.isEmpty() ? "대화가 없습니다." : messages.get(messages.size() - 1).getMessage();
            LocalDateTime lastTime = messages.isEmpty() ? room.getCreatedAt() : messages.get(messages.size() - 1).getSentAt();

            return ChatDto.RoomResponse.builder()
                    .roomId(room.getId())
                    .partnerName(partner.getNickname())
                    .lastMessage(lastMessage)
                    .lastMessageTime(lastTime)
                    .itemTitle(room.getProduct().getTitle())
                    .itemImageUrl(room.getProduct().getImageUrl())
                    .build();
        }).collect(Collectors.toList());
    }

    // 3. 메시지 전송
    @Transactional
    public ChatDto.MessageResponse sendMessage(Long roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 엔티티 생성 및 저장
        ChatMessage message = ChatMessage.create(room, sender, content);
        chatMessageRepository.save(message);

        // DTO 반환
        return ChatDto.MessageResponse.builder()
                .messageId(message.getId())
                .senderId(sender.getUserId())
                .senderName(sender.getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .isRead(message.isRead())
                .build();
    }

    // 4. 메시지 내역 조회
    public List<ChatDto.MessageResponse> getMessages(Long roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방 없음"));

        // 방 참여자인지 확인 (보안)
        if (!room.getBuyer().getUserId().equals(userId) && !room.getSeller().getUserId().equals(userId)) {
            throw new IllegalArgumentException("이 방에 접근 권한이 없습니다.");
        }

        return chatMessageRepository.findByChatRoom_IdOrderBySentAtAsc(roomId).stream()
                .map(msg -> ChatDto.MessageResponse.builder()
                        .messageId(msg.getId())
                        .senderId(msg.getSender().getUserId())
                        .senderName(msg.getSender().getNickname())
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .isRead(msg.isRead())
                        .build())
                .collect(Collectors.toList());
    }
}