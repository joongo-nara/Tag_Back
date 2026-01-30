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

    // 1. 채팅방 생성 (무조건 관리자와 연결)
    @Transactional
    public Long createChatRoom(Long userId, Long productId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // [수정 1] 판매자(상대방)를 '관리자'로 고정
        User admin = userRepository.findByEmail("admin@tag.com")
                .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 없습니다. (AdminInitializer 확인 필요)"));

        // [수정 2] 관리자 본인은 자신에게 채팅 걸 수 없음
        if (buyer.getEmail().equals("admin@tag.com")) {
            throw new IllegalArgumentException("관리자는 상품에 대해 채팅을 생성할 수 없습니다.");
        }

        // (참고) 원래 있던 '내 물건엔 채팅 불가' 로직은 삭제했습니다.
        // -> 내가 접수한 물건이라도 플랫폼(관리자)에 문의할 수 있어야 하니까요.

        // 이미 생성된 방이 있는지 확인
        return chatRoomRepository.findByProduct_ProductIdAndBuyer_UserId(productId, userId)
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    // 판매자를 admin으로 설정하여 방 생성
                    ChatRoom room = ChatRoom.createRoom(product, buyer, admin);
                    chatRoomRepository.save(room);
                    return room.getId();
                });
    }

    // 2. 내 채팅방 목록 조회
    public List<ChatDto.RoomResponse> getMyChatRooms(Long userId) {
        // 내가 구매자이거나 판매자(관리자)인 방을 모두 찾음
        List<ChatRoom> rooms = chatRoomRepository.findByBuyer_UserIdOrSeller_UserIdOrderByCreatedAtDesc(userId, userId);

        return rooms.stream().map(room -> {
            // 상대방 이름 찾기 (내가 구매자면 -> 판매자(관리자)가 상대방, 관리자면 -> 구매자가 상대방)
            User partner = room.getBuyer().getUserId().equals(userId) ? room.getSeller() : room.getBuyer();

            // 마지막 메시지 찾기
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

    // 3. 메시지 전송 (기존과 동일)
    @Transactional
    public ChatDto.MessageResponse sendMessage(Long roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        ChatMessage message = ChatMessage.create(room, sender, content);
        chatMessageRepository.save(message);

        return ChatDto.MessageResponse.builder()
                .messageId(message.getId())
                .senderId(sender.getUserId())
                .senderName(sender.getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .isRead(message.isRead())
                .build();
    }

    // 4. 메시지 내역 조회 (기존과 동일)
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