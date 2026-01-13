package com.example.tag.domain.chat.repository;

import com.example.tag.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 내가 참여중인 채팅방 목록 조회 (구매자이거나 OR 판매자이거나)
    List<ChatRoom> findByBuyer_UserIdOrSeller_UserIdOrderByCreatedAtDesc(Long buyerId, Long sellerId);

    // 특정 상품에 대해 내가 이미 만든 방이 있는지 확인 (중복 생성 방지)
    Optional<ChatRoom> findByProduct_ProductIdAndBuyer_UserId(Long productId, Long buyerId);
}