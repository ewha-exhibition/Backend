package com.example.tikitaka;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.global.exception.GlobalExceptionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// 테스트에서 Member에 대한 인증을 주입하기 위함
@TestConfiguration
@Import(GlobalExceptionHandler.class)
public class TestConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                        && Member.class.isAssignableFrom(parameter.getParameterType());
            }
            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                // 테스트 고정 멤버 반환
                return Member.builder()
                        .memberId(1L)
                        .email("h@e.com")
                        .username("heewon")
                        .profileUrl("p")
                        .path(RegisterPath.SELF)
                        .status(com.example.tikitaka.domain.member.entity.Status.ACTIVE)
                        .build();
            }
        });
    }
}
