package com.example.tikitaka.global.config.auth;
// 성공 핸들러 : 우리JWT발급 -> 프론트로 리다이렉트
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 1) 카카오 사용자 식별 → 우리 유저 조회/생성
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oAuth2User.getAttributes();

        Long kakaoId = ((Number) attrs.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

        RegisterPath path = RegisterPath.KAKAO;

        Member member = Optional.ofNullable(email)
                .flatMap(em -> memberRepository.findByEmailAndPath(em, path))
                .orElseThrow(() -> new IllegalStateException(
                        "Member not found after OAuth2 success (email consent required). kakaoId=" + kakaoId));

        // 2) 우리 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getMemberId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

        // 3) httpOnly 쿠키로 내려주기 (URL에 토큰 노출 X)
        // 로컬 개발은 Secure=false 가능하지만, 가능하면 true + https 환경 권장
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(false)      // 로컬 http면 false, 운영 https면 true
                .sameSite("Lax")    // 다른 포트/도메인 조합이면 None+CORS 설정 필요
                .path("/")
                .maxAge(60 * 60)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(14L * 24 * 60 * 60) // 14일
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // todo : refreshToken도 추가하기

        // 4) ApiResponse 사용해서 200 응답
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        var resultData = Map.of(
                "successhandler", "ok",
                "memberId", member.getMemberId(),
                "nickname", member.getUsername()
        );

        ApiResponse<Object> apiResponse = ApiResponse.onSuccess(200, resultData);

        // JSON 변환해서 응답
        String jsonBody = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonBody);





//        String jwt = jwtTokenProvider.createToken(
//                user.getId(), user.getRole(), user.getEmail()
//        );
    }
}
