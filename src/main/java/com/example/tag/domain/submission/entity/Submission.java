package com.example.tag.domain.submission.entity;

import com.example.tag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Submission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String type; // DELIVERY, PICKUP
    private Double weight;
    private Integer priceRequest;
    private String pickupAddress;
    private String proofImageUrl;

    @Builder.Default
    private String status = "REQUESTED";

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // 상태 변경
    public void updateStatus(String status) {
        this.status = status;
    }
}