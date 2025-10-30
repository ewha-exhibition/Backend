package com.example.tikitaka.global.context;

public class CurrentUserContext {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();  // thread 단위 저장(http 요청 하나당)
    public static void setUserId(Long memberId) { TL.set(memberId); } // 현재 요청 스레드에 로그인한 사용자 id 저장
    public static Long getMemberId() { return TL.get(); } // 나중에 쉽게 꺼내 쓰도록
    public static void clear() { TL.remove(); }  // 요청 종료시 메모리 누수 방지 위해 삭제
}