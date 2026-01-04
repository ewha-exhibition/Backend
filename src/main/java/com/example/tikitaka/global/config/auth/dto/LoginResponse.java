package com.example.tikitaka.global.config.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean ok;
    private Long memberId;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}


