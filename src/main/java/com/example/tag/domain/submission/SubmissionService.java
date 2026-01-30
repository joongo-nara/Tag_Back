package com.example.tag.domain.submission;

import com.example.tag.domain.product.ProductService;
import com.example.tag.domain.submission.dto.SubmissionRequestDto;
import com.example.tag.domain.submission.entity.Submission;
import com.example.tag.domain.submission.repository.SubmissionRepository;
import com.example.tag.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProductService productService;

    @Transactional
    public Long createSubmission(User user, SubmissionRequestDto dto) {
        Submission submission = Submission.builder()
                .user(user)
                .type(dto.getType())
                .weight(dto.getWeight())
                .priceRequest(dto.getPriceRequest())
                .pickupAddress(dto.getPickupAddress())
                .proofImageUrl(dto.getProofImageUrl()) // S3 연동 시 URL 받아와야 함
                .build();
        return submissionRepository.save(submission).getSubmissionId();
    }

    @Transactional
    public String approveSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("접수 내역이 없습니다."));

        // 이미 승인된 건인지 확인하는 코드
        if ("ACCEPTED".equals((submission.getStatus()))) {
            throw  new IllegalStateException("이미 승인되어 상품으로 등록된 건입니다.");
        }

        // 상태 변경 (REQUESTED -> ACCEPTED)
        submission.updateStatus("ACCEPTED");

        // 상품 생성 (ProductService 호출)
        productService.createProductFromSubmission(submission);

        return "승인 완료 및 상품 등록 성공";
    }
}