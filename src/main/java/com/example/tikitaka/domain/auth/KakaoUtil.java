package com.example.tikitaka.domain.auth;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KakaoUtil {

    @Value("${spring.kakao.auth.redirect}")
    private String client;
    @Value("${spring.kakao.auth.redirect}")
    private String redirect;
}