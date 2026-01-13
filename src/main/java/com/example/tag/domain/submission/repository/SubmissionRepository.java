package com.example.tag.domain.submission.repository;
import com.example.tag.domain.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubmissionRepository extends JpaRepository<Submission, Long> {}