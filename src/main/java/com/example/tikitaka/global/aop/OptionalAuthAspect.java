package com.example.tikitaka.global.aop;



import com.example.tikitaka.global.context.CurrentUserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class OptionalAuthAspect {

    @Around("@annotation(com.example.tikitaka.global.annotation.OptionalAuth)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() != null
                && !"anonymousUser".equals(auth.getPrincipal())) {
            userId = auth.getName(); // 위 필터에서 넣은 principal(userId)
        }

        try {
            CurrentUserContext.setUserId(userId); // null일 수도 있음(비로그인)
            return pjp.proceed();
        } finally {
            CurrentUserContext.clear(); // ★ 반드시 정리!
        }
    }
}


