package com.example.tikitaka.domain.exhibition.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

// 호스트 초대코드 생성기(util)
@Component
public class HostCodeGenerator {
    private static final char[] POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray(); // I,O,0,1 제외
    private static final int LEN = 8;
    private final SecureRandom rnd = new SecureRandom();

    public String generator() {
        StringBuilder sb = new StringBuilder(LEN);
        for (int i = 0; i < LEN; i++) sb.append(POOL[rnd.nextInt(POOL.length)]);
        return sb.toString();
    }
}
