package com.example.tikitaka.global.config.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshResponse {
    private boolean ok;
    private String accessToken;
    private String refreshToken;
}
