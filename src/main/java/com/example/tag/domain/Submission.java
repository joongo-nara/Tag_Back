package com.example.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String type; // DELIVERY, PICKUP

    @Column(nullable = false)
    private Double weight;

    private Integer priceRequest;
    private String pickupAddress;

    @Column(nullable = false, length = 500)
    private String proofImageUrl;

    @Column(nullable = false, length = 20)
    private String status = "REQUESTED";

    @Column(nullable = false)
    private LocalDateTime createdAt;
}