package com.example.tikitaka.global.config.auth;

import com.example.tikitaka.global.exception.ErrorReason;
import com.example.tikitaka.global.exception.GlobalErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // 전역 에러 코드
        GlobalErrorCode errorCode = GlobalErrorCode.ACCESS_DENIED;
        ErrorReason reason = errorCode.getErrorReason();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // JSON으로 변환해서 응답
        String responseBody = objectMapper.writeValueAsString(reason);
        response.getWriter().write(responseBody);
    }
}

