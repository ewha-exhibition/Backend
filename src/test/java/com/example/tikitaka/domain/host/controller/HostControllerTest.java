package com.example.tikitaka.domain.host.controller;

import com.example.tikitaka.TestConfig;
import com.example.tikitaka.domain.host.HostErrorCode;
import com.example.tikitaka.domain.host.service.HostService;
import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;
import com.example.tikitaka.global.config.auth.jwt.JwtAuthFilter;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.exception.BaseErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestConfig.class)
@WebMvcTest(controllers = HostController.class)
@AutoConfigureMockMvc(addFilters = false)
class HostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HostService hostService;

    // Jwt 관련 빈들을 목으로 주입해서 컨텍스트 에러 방지
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;





    @Test
    @DisplayName("POST /hosts/join - 성공 시 204")
    void join_success_204() throws Exception {
        mockMvc.perform(post("/hosts/join").param("code", "ABCDEFGH"))
                .andExpect(status().isNoContent());
        Mockito.verify(hostService).joinByInviteCode(1L, "ABCDEFGH");
    }

    @Test
    @DisplayName("POST /hosts/join - 코드없음 404")
    void join_code_not_found_404() throws Exception {
        doThrow(new BaseErrorException(HostErrorCode.CODE_NOT_FOUND))
                .when(hostService).joinByInviteCode(anyLong(), Mockito.eq("NOTEXIST"));

        mockMvc.perform(post("/hosts/join").param("code", "NOTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /hosts/join - 이미 호스트 409")
    void join_already_host_409() throws Exception {
        doThrow(new BaseErrorException(HostErrorCode.ALREADY_HOST))
                .when(hostService).joinByInviteCode(anyLong(), Mockito.eq("ABCDEFGH"));

        mockMvc.perform(post("/hosts/join").param("code", "ABCDEFGH"))
                .andExpect(status().isConflict());
    }

}