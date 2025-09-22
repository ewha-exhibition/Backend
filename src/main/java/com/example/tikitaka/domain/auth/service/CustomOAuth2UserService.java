package com.example.tikitaka.domain.auth.service; // 본인의 프로젝트 패키지 경로에 맞게 수정하세요.

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.repository.MemberRepository;
import com.example.tikitaka.domain.auth.dto.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService를 생성합니다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        // 기본 서비스를 이용해 소셜 로그인 플랫폼으로부터 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 1. 현재 로그인 진행 중인 서비스를 구분하는 코드 (e.g., "kakao", "google")
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 2. OAuth2 로그인 진행 시 키가 되는 필드값 (PK와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 3. OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스입니다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 4. 데이터베이스에서 이메일을 기반으로 사용자를 찾거나, 없으면 새로 생성합니다.
        Member member = saveOrUpdate(attributes);
        System.out.println("유저출력 : " + member);

        // 5. 세션에 사용자 정보를 저장합니다. (세션 DTO를 따로 만드는 것이 더 좋습니다)
        // 세션을 사용하지 않으므로 삭제

//        httpSession.setAttribute("user", new SessionUser(user)); // SessionUser는 직렬화 가능한 DTO

        // 최종적으로 인증된 사용자 정보를 담은 DefaultOAuth2User 객체를 반환합니다.
        // 이 객체는 Spring Security의 SecurityContext에 저장되어, 애플리케이션 전반에서 사용됩니다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 데이터베이스에 사용자를 저장하거나 업데이트하는 메소드
    private Member saveOrUpdate(OAuthAttributes attributes) {
        System.out.println("saveorupdate : 들어는왔다 ");
        Member user = memberRepository.findByProviderAndProviderId(attributes.getProvider(), attributes.getProviderId())
                // 이미 가입된 사용자인 경우, 이름과 프로필 사진을 업데이트합니다.
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                // 가입되지 않은 사용자인 경우, OAuthAttributes를 User 엔티티로 변환하여 새로 생성합니다.
                .orElse(attributes.toEntity());

        return memberRepository.save(user);
    }
}