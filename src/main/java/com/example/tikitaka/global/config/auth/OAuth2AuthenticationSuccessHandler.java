package com.example.tikitaka.global.config.auth;
// 성공 핸들러 : 우리JWT발급 -> 프론트로 리다이렉트
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.config.auth.user.User;
import com.example.tikitaka.global.config.auth.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // DefaultOAuth2User에서 카카오 id 꺼내 우리 User 찾기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long kakaoId = ((Number) oAuth2User.getAttributes().get("id")).longValue();

        User user = userRepository.findByProviderAndProviderId("KAKAO", String.valueOf(kakaoId))
                .orElseThrow(); // 이 시점엔 있어야 정상

        // 우리 JWT 발급 (sub=user.id 등)
        String jwt = jwtTokenProvider.createToken(
                user.getId(), user.getRole(), user.getEmail()
        );

        // 프론트 콜백 URL로 전달 (예: http://localhost:3000/oauth/callback)
        String target = UriComponentsBuilder
                .fromUriString("http://localhost:3000/oauth/callback")
                .queryParam("token", jwt)
                .build()
                .toUriString();

        response.sendRedirect(target);
    }
}
