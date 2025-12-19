package com.example.tikitaka.global.config.auth;

import com.example.tikitaka.global.config.auth.dto.AuthResponse;
import com.example.tikitaka.global.config.auth.dto.KakaoCodeRequest;

import com.example.tikitaka.global.config.auth.dto.LoginResponse;
import com.example.tikitaka.global.config.auth.dto.RefreshResponse;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    // 프론트: /oauth/callback 에서 받은 code와 redirectUri를 여기에 POST
    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> loginWithKakao(@RequestBody KakaoCodeRequest req) {
        AuthResponse result = authService.loginWithKakao(req);
        // 5) httpOnly 쿠키로 내려주기 (프론트는 로컬스토리지에 저장할 필요 없음)
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", result.getAccessToken())
                .httpOnly(true)
                .secure(false)      // 운영 HTTPS면 true 권장
                .sameSite("Lax")    // SPA 크로스도메인이면 'None' + secure=true 설정 필요
                .path("/")
                .maxAge(60 * 60)    // 1시간 예시
                .build();

        // Refresh Token 쿠키 (예: 14일)
        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(14L * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new LoginResponse(
                        true,
                        result.getMemberId(),
                        result.getNickname(),
                        result.getAccessToken(),
                        result.getRefreshToken()
                ));

    }

    /**
     * Refresh Token으로 Access/Refresh 재발급
     * - FETCH: POST /api/auth/refresh (쿠키 자동 포함)
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "ok", false,
                            "error", "refresh_token_missing"
                    ));
        }

        AuthResponse result;
        try {
            result = authService.reissueTokens(refreshToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "ok", false,
                            "error", e.getMessage()
                    ));
        }

        ResponseCookie newAccessCookie = ResponseCookie.from("ACCESS_TOKEN", result.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(60 * 60)
                .build();

        ResponseCookie newRefreshCookie = ResponseCookie.from("REFRESH_TOKEN", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(14L * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body(new RefreshResponse(
                        true,
                        result.getAccessToken(),
                        result.getRefreshToken()
                ));

    }
    @GetMapping("/test-auth/token/{memberId}")
    public Map<String, String> issue(@PathVariable Long memberId) {
        String token = jwtTokenProvider.createAccessToken(memberId);
        return Map.of("accessToken", token);
    }

    // todo : 로그아웃 시, 쿠키 제거 로직(2차)

}
