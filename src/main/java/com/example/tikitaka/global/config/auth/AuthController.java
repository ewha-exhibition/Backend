package com.example.tikitaka.global.config.auth;

import com.example.tikitaka.global.config.auth.dto.AuthResponse;
import com.example.tikitaka.global.config.auth.dto.KakaoCodeRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // 프론트: /oauth/callback 에서 받은 code와 redirectUri를 여기에 POST
    @PostMapping("/kakao")
    public ResponseEntity<?> loginWithKakao(@RequestBody KakaoCodeRequest req) {
        AuthResponse result = authService.loginWithKakao(req);
        // 5) httpOnly 쿠키로 내려주기 (프론트는 로컬스토리지에 저장할 필요 없음)
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", result.getAccessToken())
                .httpOnly(true)
                .secure(false)      // 운영 HTTPS면 true 권장
                .sameSite("Lax")    // SPA 크로스도메인이면 'None' + secure=true 설정 필요
                .path("/")
                .maxAge(60 * 60)    // 1시간 예시
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", accessCookie.toString())
                .body(Map.of(
                        "ok", true,
                        "memberId", result.getMemberId(),
                        "nickname", result.getNickname()
                ));
    }
}
