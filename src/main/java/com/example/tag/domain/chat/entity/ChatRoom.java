package com.example.tag.domain.chat.entity;

import com.example.tag.domain.product.entity.Product;
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
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    // 어떤 상품에 대한 채팅인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 구매자 (채팅을 건 사람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    // 판매자 (상품 주인)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 채팅방 생성 메서드
    public static ChatRoom createRoom(Product product, User buyer, User seller) {
        ChatRoom room = new ChatRoom();
        room.product = product;
        room.buyer = buyer;
        room.seller = seller;
        return room;
    }
}