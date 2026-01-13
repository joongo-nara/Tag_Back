package com.example.tag.domain.wishlist;

import com.example.tag.domain.wishlist.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final com.example.tag.domain.user.repository.UserRepository userRepository;

    // 찜하기 (또는 취소)
    @PostMapping
    public ResponseEntity<String> toggleWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody WishlistDto.Request request) {

        // 이메일로 유저 ID 찾기 (간단한 구현을 위해)
        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();

        String result = wishlistService.toggleWishlist(userId, request.getItemId());
        return ResponseEntity.ok(result);
    }

    // 내 찜 목록 조회
    @GetMapping("/me")
    public ResponseEntity<List<WishlistDto.Response>> getMyWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getUserId();

        return ResponseEntity.ok(wishlistService.getMyWishlist(userId));
    }
}