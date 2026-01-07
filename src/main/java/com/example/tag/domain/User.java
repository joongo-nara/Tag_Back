package com.example.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 100)
    private String email;

    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String provider;

    private String providerId;

    @Column(nullable = false)
    private String address;

    private String clothingSize;

    @Column(nullable = false, length = 20)
    private String role; // USER, ADMIN
}