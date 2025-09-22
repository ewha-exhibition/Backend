package com.example.tikitaka.domain.auth.dto;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.domain.member.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

    // 원본 attributes가 필요 없으면 이 필드/생성 파라미터는 제거해도 됩니다.
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;   // 보통 "id"
    private final String email;              // Member.email
    private final String username;           // Member.username
    private final String profileUrl;         // Member.profileUrl
    private final RegisterPath path;         // Member.path (Enum: KAKAO/GOOGLE/...)

    /** 카카오 응답(JSON) → DTO */
    @SuppressWarnings("unchecked")
    public static OAuthAttributes fromKakao(String nameAttributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null
                ? (Map<String, Object>) kakaoAccount.get("profile")
                : null;

        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        String username = profile != null ? (String) profile.get("nickname") : null;
        String profileUrl = profile != null ? (String) profile.get("profile_image_url") : null;

        // 필요 시 기본값 보정
        if (username == null || username.isBlank()) {
            username = "user-" + attributes.get("id");
        }
        if (profileUrl == null) profileUrl = "";

        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey) // 보통 "id"
                .email(email)                       // 이메일 동의 안 받았으면 null일 수 있음
                .username(username)
                .profileUrl(profileUrl)
                .path(RegisterPath.KAKAO)           // 카카오 고정
                .build();
    }

    /** DTO → Member 엔티티 */
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .username(username)
                .profileUrl(profileUrl)
                .path(path)
                .status(Status.ACTIVE) // 프로젝트 규칙에 맞게 기본값 지정
                .build();
    }
}
