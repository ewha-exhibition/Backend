package com.example.tikitaka.global.config.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoCodeRequest {
    private String code;
    private String redirectUri;
}
