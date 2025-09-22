package com.example.tikitaka.domain.auth;

import com.example.tikitaka.global.config.jwt.JwtTokenProviders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // JWT 토큰을 생성하는 유틸리티 클래스
     private final JwtTokenProviders jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 1) JWT 생성
        String jwt = jwtTokenProvider.generateToken(authentication);

        // 2) HttpOnly + Secure + SameSite=None 쿠키 설정 (크로스 도메인 프론트용)
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", jwt)
                .httpOnly(true)
                .secure(true)               // https에서만
                .path("/")                  // FE에서 필요한 경로
                .sameSite("None")           // FE가 다른 도메인일 때 필요
                .maxAge(Duration.ofHours(1))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // 3) 프론트로 리다이렉트 (토큰은 URL에 노출하지 않음)
        String target = "http://localhost:3000/oauth/callback?ok=1";
        getRedirectStrategy().sendRedirect(request, response, target);
    }

}