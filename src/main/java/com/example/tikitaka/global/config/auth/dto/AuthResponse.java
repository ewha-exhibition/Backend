package com.example.tikitaka.global.config.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Long memberId;
    private String nickname;
    private String accessToken;
}

