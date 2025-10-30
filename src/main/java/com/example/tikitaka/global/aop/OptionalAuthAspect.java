package com.example.tikitaka.global.aop;



import com.example.tikitaka.global.context.CurrentUserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class OptionalAuthAspect {
    @Around("@annotation(com.example.tikitaka.global.annotation.OptionalAuth)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)
                && auth.getPrincipal() != null) {

            Object principal = auth.getPrincipal();

            if (principal instanceof Long id) {          // ← 지금 구조(주입이 Long)와 일치
                userId = id;
            } else if (principal instanceof String s) {  // 혹시 String인 경우 대비
                if (!"anonymousUser".equals(s)) {
                    try { userId = Long.valueOf(s); } catch (NumberFormatException ignore) {}
                }
            }
        }

        try {
            CurrentUserContext.setUserId(userId); // null이면 비로그인
            return pjp.proceed();
        } finally {
            CurrentUserContext.clear();
        }
    }
}

