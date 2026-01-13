package com.example.tag.domain.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WishlistDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private Long itemId; // 어떤 상품을 찜할지
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long wishlistId;
        private Long itemId;
        private String itemName;
        private int price;
        private String itemImageUrl; // 대표 이미지
    }
}