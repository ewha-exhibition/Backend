package com.example.tikitaka.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RegisterPath {
    KAKAO("카카오"),
    GOOGLE("구글"),
    NAVER("네이버"),
    SELF("자체");

    @JsonValue
    private final String path;

    RegisterPath(String path) {
        this.path = path;
    }


}
