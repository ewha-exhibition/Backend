package com.example.tikitaka.domain.auth.controller;

import com.example.tikitaka.domain.auth.service.KakaoApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoController {

    private final KakaoApiService kakaoApiService;

    public KakaoController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return kakaoApiService.getUserProfile();
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return kakaoApiService.logout();
    }

    @GetMapping("/unlink")
    public ResponseEntity<?> unlink() {
        return kakaoApiService.unlink();
    }
}