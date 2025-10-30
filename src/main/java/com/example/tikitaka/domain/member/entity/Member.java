package com.example.tikitaka.domain.member.entity;

import com.example.tikitaka.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "register_path", nullable = false)
    private RegisterPath path;

    @Enumerated
    @Column(name = "status",  nullable = false)
    private Status status;

    public Member updateSocialProfile(String newName, String newProfileUrl) {
        if (newName != null && !newName.isBlank()) this.username = newName;
        if (newProfileUrl != null && !newProfileUrl.isBlank()) this.profileUrl = newProfileUrl;
        return this;
    }

}


