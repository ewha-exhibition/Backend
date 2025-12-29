package com.example.tikitaka.global.config.auth;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.entity.Status;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.config.auth.dto.AuthResponse;
import com.example.tikitaka.global.config.auth.dto.KakaoCodeRequest;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // WebClient 는 빈으로 주입해도 되고, 간단히 빌더로 만들어도 됩니다.
    private final WebClient webClient = WebClient.builder().build();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret:}")
    private String kakaoClientSecret; // 비어있을 수 있음(옵션)

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri:https://kauth.kakao.com/oauth/token}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri:https://kapi.kakao.com/v2/user/me}")
    private String kakaoUserInfoUri;

    @Transactional
    public AuthResponse loginWithKakao(KakaoCodeRequest req) {
        // 1) tokenResp 요청
        // 2) userInfo 요청
        // 3) member 저장
        // 4) jwt 발급
        // 로직을 그대로 옮기면 됨


        // 1) 카카오 액세스 토큰 교환 (x-www-form-urlencoded)
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", kakaoClientId);
        if (kakaoClientSecret != null && !kakaoClientSecret.isBlank()) {
            form.add("client_secret", kakaoClientSecret);
        }
        form.add("redirect_uri", req.getRedirectUri()); // 프론트 콜백(카카오 콘솔 등록된 값과 동일)
        form.add("code", req.getCode());

        Map<String, Object> tokenResp = webClient.post()
                .uri(kakaoTokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(form)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (tokenResp == null || tokenResp.get("access_token") == null) {
            throw new RuntimeException("token_exchange_failed");  // global error?
        }
        String kakaoAccessToken = tokenResp.get("access_token").toString();

        // 2) 카카오 사용자 정보 조회
        Map<String, Object> userInfo = webClient.get()
                .uri(kakaoUserInfoUri)
                .headers(h -> h.setBearerAuth(kakaoAccessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();



        if (userInfo == null || userInfo.get("id") == null) {
            throw new RuntimeException("user_info_failed");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        if (kakaoAccount == null) {
            throw new RuntimeException("kakao_account_missing");
        }

        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile != null ? (String) profile.get("nickname") : "카카오사용자";
        String profileUrl = "";
        if (profile != null) {
            profileUrl = (String) profile.getOrDefault("profile_image_url", "");
            if (profileUrl.isBlank()) {
                profileUrl = (String) profile.getOrDefault("thumbnail_image_url", "");
            }
        }

        // 3) 우리 유저 upsert (provider=KAKAO, providerId=kakaoId)
        RegisterPath path = RegisterPath.KAKAO;
        String finalProfileUrl = profileUrl;
        Member member = memberRepository.findByEmailAndPath(email, path)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(email)
                                .username(nickname)
                                .profileUrl(finalProfileUrl)
                                .path(path)
                                .status(Status.ACTIVE)
                                .build()
                ));


        // 4) JWT 발급 (access + refresh)
        String accessToken = jwtTokenProvider.createAccessToken(member.getMemberId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

        // 5) 컨트롤러로 돌려줄 간단한 DTO
        return new AuthResponse(member.getMemberId(), member.getUsername(), accessToken, refreshToken);

    }

    /**
     * refresh 토큰으로 새 Access/Refresh 발급
     */
    @Transactional
    public AuthResponse reissueTokens(String refreshToken) {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("invalid_refresh_token");
        }

        Long memberId = jwtTokenProvider.getMemberId(refreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member_not_found"));

        String newAccessToken = jwtTokenProvider.createAccessToken(member.getMemberId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

        return new AuthResponse(member.getMemberId(), member.getUsername(), newAccessToken, newRefreshToken);
    }
}
