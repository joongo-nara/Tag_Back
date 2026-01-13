package com.example.tag.domain.product.entity;

import com.example.tag.domain.submission.entity.Submission;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "products")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    private String title;
    private String category;
    private String size;
    private String conditionStatus;
    private Integer price;
    private String imageUrl;

    @Builder.Default
    private String status = "AVAILABLE";
}