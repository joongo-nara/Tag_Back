package com.example.tag.domain.submission.dto;

import com.example.tag.domain.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResponseDto {

    private Long submissionId;
    private String type;          // "DELIVERY"(택배) or "PICKUP"(수거)
    private String status;        // "INSPECTING", "COMPLETED" 등
    private Double weight;        // 무게
    private Integer priceRequest; // 정산 희망가 (택배인 경우)
    private String pickupAddress; // 수거 주소 (수거인 경우)
    private String proofImageUrl; // 접수 인증 사진 URL
    private LocalDateTime createdAt;

    // Entity -> Dto 변환 메서드
    public static SubmissionResponseDto from(Submission submission) {
        return SubmissionResponseDto.builder()
                .submissionId(submission.getSubmissionId())
                .type(submission.getType()) // Enum인 경우 .name()
                .status(submission.getStatus())
                .weight(submission.getWeight())
                .priceRequest(submission.getPriceRequest())
                .pickupAddress(submission.getPickupAddress())
                .proofImageUrl(submission.getProofImageUrl())
                .createdAt(submission.getCreatedAt())
                .build();
    }
}