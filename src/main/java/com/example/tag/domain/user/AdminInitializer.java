package com.example.tag.domain.user;

import com.example.tag.domain.user.entity.User;
import com.example.tag.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 관리자 계정이 없으면 하나 만듦
        if (userRepository.findByEmail("admin@tag.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@tag.com")
                    .password(passwordEncoder.encode("admin1234")) // 비밀번호 고정
                    .nickname("TAG관리자")
                    .address("본사")
                    .role("ADMIN") // 역할: 관리자
                    .provider("LOCAL") // 가입 경로
                    .providerId("ADMIN_ID") // 식별자
                    .build();
            userRepository.save(admin);
            System.out.println("[System] 관리자 계정 생성 완료: admin@tag.com");
        }
    }
}