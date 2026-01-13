package com.example.tag.domain.user.dto;

import com.example.tag.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long userId;
    private String email;
    private String nickname;
    private String address;
    private String clothingSize; // "L", "XL" 등
    private String role;         // "USER", "ADMIN"

    // Entity -> Dto 변환 메서드 (편의상 추가)
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .clothingSize(user.getClothingSize())
                .role(user.getRole())
                .build();
    }
}