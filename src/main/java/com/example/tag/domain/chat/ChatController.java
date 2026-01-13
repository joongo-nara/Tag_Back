package com.example.tag.domain.chat;

import com.example.tag.domain.chat.dto.ChatDto;
import com.example.tag.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    // 1. 채팅방 생성 (상품 상세페이지에서 '문의하기' 눌렀을 때)
    @PostMapping("/room")
    public ResponseEntity<Long> createRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatDto.RoomRequest request) {

        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();
        Long roomId = chatService.createChatRoom(userId, request.getItemId());
        return ResponseEntity.ok(roomId);
    }

    // 2. 내 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatDto.RoomResponse>> getMyRooms(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();
        return ResponseEntity.ok(chatService.getMyChatRooms(userId));
    }

    // 3. 특정 채팅방 메시지 내역 조회
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatDto.MessageResponse>> getMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long roomId) {

        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();
        return ResponseEntity.ok(chatService.getMessages(roomId, userId));
    }

    // 4. 메시지 전송 (일단 HTTP API로 구현)
    @PostMapping("/room/message")
    public ResponseEntity<ChatDto.MessageResponse> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatDto.MessageRequest request) {

        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();
        return ResponseEntity.ok(chatService.sendMessage(request.getRoomId(), userId, request.getMessage()));
    }
}