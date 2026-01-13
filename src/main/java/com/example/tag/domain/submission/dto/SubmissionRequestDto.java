package com.example.tag.domain.submission.dto;
import lombok.Data;

@Data
public class SubmissionRequestDto {
    private String type;
    private Double weight;
    private Integer priceRequest;
    private String pickupAddress;
    private String proofImageUrl;
}