package com.example.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 10)
    private String size;

    @Column(nullable = false, length = 20)
    private String conditionStatus; // S, A, B급

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 20)
    private String status = "AVAILABLE";
}