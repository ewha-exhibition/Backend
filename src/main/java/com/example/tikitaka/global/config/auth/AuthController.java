package com.example.tikitaka.global.config.auth;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.entity.Status;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.config.auth.user.User;
import com.example.tikitaka.global.config.auth.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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

    // 프론트: /oauth/callback 에서 받은 code와 redirectUri를 여기에 POST
    @PostMapping("/kakao")
    @Transactional
    public ResponseEntity<?> loginWithKakao(@RequestBody KakaoCodeRequest req) {

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
            return ResponseEntity.badRequest().body(Map.of("error", "token_exchange_failed"));
        }
        String kakaoAccessToken = tokenResp.get("access_token").toString();

        // 2) 카카오 사용자 정보 조회
        Map<String, Object> userInfo = webClient.get()
                .uri(kakaoUserInfoUri)
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.info("Kakao userInfo = {}", userInfo);
        System.out.println("=== Kakao userInfo ===");
        System.out.println("userInfo = " + userInfo);


        if (userInfo == null || userInfo.get("id") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "user_info_failed"));
        }

        Long kakaoId = ((Number) userInfo.get("id")).longValue();

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        if (kakaoAccount == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "kakao_account_missing"));
        }
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        System.out.println("profile keys: " + profile.keySet());

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


        // 4) JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getMemberId());

        // 5) httpOnly 쿠키로 내려주기 (프론트는 로컬스토리지에 저장할 필요 없음)
        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
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
                        "memberId", member.getMemberId(),
                        "nickname", member.getUsername()
                ));
    }

    @Getter @Setter
    public static class KakaoCodeRequest {
        private String code;
        private String redirectUri;
    }
}
