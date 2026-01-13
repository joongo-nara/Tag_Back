package com.example.tag.domain.submission;

import com.example.tag.domain.submission.dto.SubmissionRequestDto;
import com.example.tag.domain.user.entity.User;
import com.example.tag.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Long> createSubmission(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SubmissionRequestDto dto) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(submissionService.createSubmission(user, dto));
    }

    @PostMapping("/{submissionId}/approve")
    public ResponseEntity<String> approveSubmission(@PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.approveSubmission(submissionId));
    }
}