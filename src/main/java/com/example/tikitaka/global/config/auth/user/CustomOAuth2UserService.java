package com.example.tikitaka.global.config.auth.user;
// 카카오 유저 -> 우리 USER 저장/업데이트
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(req);

        String registrationId = req.getClientRegistration().getRegistrationId(); // kakao
        String nameAttributeKey = req.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();               // "id"

        Map<String, Object> attrs = oAuth2User.getAttributes();
        // 카카오 응답 파싱
        Long kakaoId = ((Number) attrs.get("id")).longValue();
        Map<String,Object> properties = (Map<String,Object>) attrs.get("properties");
        String nickname = properties != null ? (String) properties.getOrDefault("nickname", "카카오유저") : "카카오유저";
        Map<String,Object> account = (Map<String,Object>) attrs.get("kakao_account");
        String email = account != null ? (String) account.get("email") : null;
        String profileImageUrl;
        if (account != null && account.containsKey("profile")) {
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");
            profileImageUrl = (String) profile.getOrDefault("profile_image_url", "");
        } else {
            profileImageUrl = "";
        }

        // 이메일이 없는 경우 대비 (nullable=false 제약 때문에)
        if (email == null) {
            email = "kakao-" + kakaoId + "@no-email.local"; // 임시 생성 정책
        }
        // DB 저장/업데이트
//        User user = userRepository.findByProviderAndProviderId("KAKAO", String.valueOf(kakaoId))
//                .map(u -> u.updateFromKakao(nickname, email))
//                .orElseGet(() -> User.fromKakao(kakaoId, email, nickname));
//        userRepository.save(user);

        RegisterPath path = RegisterPath.KAKAO;

        String finalEmail = email;
        Member member = memberRepository.findByEmailAndPath(email, path)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(finalEmail)
                                .username(nonEmpty(nickname, "카카오사용자"))
                                .profileUrl(nonEmpty(profileImageUrl, ""))
                                .path(path)
                                .status(Status.ACTIVE)
                                .build()
                ));

        System.out.println("member"+ member);

        // SecurityContext 에 넣을 principal (권한 포함)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attrs,
                nameAttributeKey
        );
    }
    private static String nonEmpty(String v, String fallback) {
        return (v == null || v.isBlank()) ? fallback : v;
    }
}
