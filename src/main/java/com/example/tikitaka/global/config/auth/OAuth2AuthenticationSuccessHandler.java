package com.example.tikitaka.global.config.auth;
// 성공 핸들러 : 우리JWT발급 -> 프론트로 리다이렉트
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.config.auth.user.User;
import com.example.tikitaka.global.config.auth.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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
        // 1) 카카오 사용자 식별 → 우리 유저 조회/생성
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long kakaoId = ((Number) oAuth2User.getAttributes().get("id")).longValue();

        User user = userRepository.findByProviderAndProviderId("KAKAO", String.valueOf(kakaoId))
                .orElseThrow(); // 이 시점엔 있어야 정상

        // 2) 우리 토큰 발급
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole(), user.getEmail());

        // 3) httpOnly 쿠키로 내려주기 (URL에 토큰 노출 X)
        // 로컬 개발은 Secure=false 가능하지만, 가능하면 true + https 환경 권장
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(false)      // 로컬 http면 false, 운영 https면 true
                .sameSite("Lax")    // 다른 포트/도메인 조합이면 None+CORS 설정 필요
                .path("/")
                .maxAge(60 * 60)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());

        // todo : refreshToken도 추가하기

        // 4) 프론트로 안전하게 리다이렉트 (토큰 쿼리스트링 x)
        String target = UriComponentsBuilder
                .fromUriString("http://localhost:3000/post-login")  // 화면 전환만 담당
                .build()
                .toUriString();



//        String jwt = jwtTokenProvider.createToken(
//                user.getId(), user.getRole(), user.getEmail()
//        );



        response.sendRedirect(target);
    }
}
