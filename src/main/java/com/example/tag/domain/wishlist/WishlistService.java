package com.example.tag.domain.wishlist;

import com.example.tag.domain.product.entity.Product;
import com.example.tag.domain.product.repository.ProductRepository;
import com.example.tag.domain.user.entity.User;
import com.example.tag.domain.user.repository.UserRepository;
import com.example.tag.domain.wishlist.dto.WishlistDto;
import com.example.tag.domain.wishlist.entity.Wishlist;
import com.example.tag.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 찜하기 (Toggle 방식: 없으면 찜, 있으면 취소)
    @Transactional
    public String toggleWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 이미 찜했는지 확인
        if (wishlistRepository.existsByUser_UserIdAndProduct_ProductId(userId, productId)) {
            // 있으면 삭제 (찜 취소)
            Wishlist wishlist = wishlistRepository.findByUser_UserIdAndProduct_ProductId(userId, productId)
                    .orElseThrow();
            wishlistRepository.delete(wishlist);
            return "찜 취소 완료";
        } else {
            // 없으면 저장 (찜 하기)
            Wishlist wishlist = Wishlist.createWishlist(user, product);
            wishlistRepository.save(wishlist);
            return "찜 하기 완료";
        }
    }

    // 내 찜 목록 조회
    public List<WishlistDto.Response> getMyWishlist(Long userId) {
        return wishlistRepository.findAllByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(wishlist -> WishlistDto.Response.builder()
                        .wishlistId(wishlist.getId())
                        .itemId(wishlist.getProduct().getProductId())
                        .itemName(wishlist.getProduct().getTitle()) // 상품명
                        .price(wishlist.getProduct().getPrice())    // 가격
                        .itemImageUrl(wishlist.getProduct().getImageUrl()) // 이미지
                        .build())
                .collect(Collectors.toList());
    }
}