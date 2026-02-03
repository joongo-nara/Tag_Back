package com.example.tag.global.config;

import com.example.tag.global.jwt.JwtAuthenticationFilter;
import com.example.tag.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 비활성화 (REST API는 보통 비활성화)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함(JWT 사용)
                .authorizeHttpRequests(auth -> auth
                        // 1. 회원가입, 로그인은 누구나 접근 가능
                        .requestMatchers("/api/user/signup", "/api/user/login").permitAll()

                        // 2. ★★★ Swagger 관련 페이지 접근 허용 (이게 없어서 403 뜸!) ★★★
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 3. 상품 등록 관리자 권한
                        .requestMatchers("/api/submission/*/approve").hasAuthority("ADMIN")

                        // 4. 나머지는 모두 인증(로그인) 필요
                        .anyRequest().authenticated()
                )
                // JWT 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}