package com.example.tikitaka.global.config.auth.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue
    private Long id;

    private String provider;    // "KAKAO"
    private String providerId;  // kakao id (문자열)
    private String email;
    private String nickname;
    private String role;        // "ROLE_USER"

    public static User fromKakao(Long kakaoId, String email, String nickname) {
        return User.builder()
                .provider("KAKAO")
                .providerId(String.valueOf(kakaoId))
                .email(email)
                .nickname(nickname)
                .role("ROLE_USER")
                .build();
    }

    public User updateFromKakao(String nickname, String email) {
        if (nickname != null) this.nickname = nickname;
        if (email != null) this.email = email;
        return this;
    }
}
