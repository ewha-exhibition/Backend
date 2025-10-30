package com.example.tikitaka.global.config.auth;

// 카카오 사용자 정보를 우리 "Member"로 저장/업데이트
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.entity.Status;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(req);

        // ex) registrationId = "kakao", nameAttributeKey = "id"
        String registrationId = req.getClientRegistration().getRegistrationId();
        String nameAttributeKey = req.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attrs = oAuth2User.getAttributes();

        // ---- 카카오 응답 파싱 ----
        Long kakaoId = ((Number) attrs.get("id")).longValue();

        Map<String, Object> account = (Map<String, Object>) attrs.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) attrs.get("properties");

        String nickname = properties != null
                ? (String) properties.getOrDefault("nickname", "카카오사용자")
                : "카카오사용자";

        String email = account != null ? (String) account.get("email") : null;
        if (email == null || email.isBlank()) {
            // 이메일 미동의/비제공 케이스 대비(엔티티 제약 회피를 위한 정책)
            email = "kakao-" + kakaoId + "@no-email.local";
        }

        String profileImageUrl = "";
        if (account != null && account.containsKey("profile")) {
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");
            profileImageUrl = (String) profile.getOrDefault("profile_image_url", "");
            if (profileImageUrl.isBlank()) {
                profileImageUrl = (String) profile.getOrDefault("thumbnail_image_url", "");
            }
        }

        // ---- Member upsert ----
        RegisterPath path = RegisterPath.KAKAO;
        final String finalEmail = email;
        final String finalNickname = nonEmpty(nickname, "카카오사용자");
        final String finalProfileUrl = nonEmpty(profileImageUrl, "");

        Member member = memberRepository.findByEmailAndPath(finalEmail, path)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(finalEmail)
                                .username(finalNickname)
                                .profileUrl(finalProfileUrl)
                                .path(path)
                                .status(Status.ACTIVE)
                                .build()
                ));

        // ---- SecurityContext에 넣을 Principal ----
        // 기본 attrs에 우리 시스템 식별자도 얹어주면 프론트/핸들러에서 쓰기 편함
        Map<String, Object> principalAttrs = new HashMap<>(attrs);
        principalAttrs.put("memberId", member.getMemberId());
        principalAttrs.put("username", member.getUsername());
        principalAttrs.put("profileUrl", member.getProfileUrl());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                principalAttrs,
                nameAttributeKey // 보통 "id"
        );
    }

    private static String nonEmpty(String v, String fallback) {
        return (v == null || v.isBlank()) ? fallback : v;
    }
}
