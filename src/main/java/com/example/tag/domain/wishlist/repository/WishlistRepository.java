package com.example.tag.domain.wishlist.repository;

import com.example.tag.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // 내 찜 목록 조회 (최신순)
    List<Wishlist> findAllByUser_UserIdOrderByCreatedAtDesc(Long userId);

    // 이미 찜한 상품인지 확인
    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    // 찜 취소를 위해 조회
    Optional<Wishlist> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}