package com.example.tikitaka.domain.scrap.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.tikitaka.domain.member.entity.Member;
import com.example.tikitaka.domain.member.entity.RegisterPath;

import com.example.tikitaka.domain.scrap.dto.ScrapListItemDto;
import com.example.tikitaka.domain.scrap.dto.response.ScrapListResponseDto;
import com.example.tikitaka.global.config.auth.jwt.JwtAuthFilter;
import com.example.tikitaka.global.config.auth.jwt.JwtTokenProvider;
import com.example.tikitaka.global.dto.PageInfo;
import com.example.tikitaka.domain.scrap.service.ScrapService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Security 설정이 있다면 추가 설정 필요. (테스트용 SecurityConfig 등)
@ActiveProfiles("test")
@WebMvcTest(ScrapController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.data.jpa.repositories.enabled=false")
class ScrapControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean
    ScrapService scrapService;

    // Jwt 관련 빈들을 목으로 주입해서 컨텍스트 에러 방지
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    // @AuthenticationPrincipal Member 파라미터를 채워줄 테스트용 ArgumentResolver
    @TestConfiguration
    static class TestConfig implements WebMvcConfigurer {
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

    private ScrapListResponseDto stubResponse(int pageNum, int limit) {
        List<ScrapListItemDto> items = List.of(
                ScrapListItemDto.builder()
                        .exhibitionId(1L)
                        .exhibitionName("전시A")
                        .posterUrl("u")
                        .place("p")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .isViewed(false)
                        .build()
        );

        return ScrapListResponseDto.builder()
                .username("heewon")
                .exhibitions(items)
                .pageInfo(PageInfo.of(pageNum, limit, 2, 6))
                .build();
    }

    @Test
    void 기본_10개_응답_JSON_검증() throws Exception {
        Mockito.when(scrapService.findScrapList(Mockito.anyLong(), Mockito.eq(1), Mockito.eq(10)))
                .thenReturn(stubResponse(1,10));

        // 인증 주입이 필요하면 Security Mocking 또는 커스텀 ArgumentResolver 사용
        mockMvc.perform(get("/scraps")
                        .requestAttr(AuthenticationPrincipal.class.getName(), // 간략화 예시
                                Member.builder().memberId(1L).username("heewon").email("h@e.com").profileUrl("p").build())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", is("heewon")))
                .andExpect(jsonPath("$.data.exhibitions", hasSize(1)))
                .andExpect(jsonPath("$.data.pageInfo.limit", is(10)))
                .andExpect(jsonPath("$.data.pageInfo.totalPages", is(2)));
    }

    @Test
    void limit_5_응답_JSON_검증() throws Exception {
        Mockito.when(scrapService.findScrapList(Mockito.anyLong(), Mockito.eq(2), Mockito.eq(5)))
                .thenReturn(stubResponse(2,5));

        mockMvc.perform(get("/scraps?pageNum=2&limit=5")
                        .requestAttr(AuthenticationPrincipal.class.getName(),
                                Member.builder().memberId(1L).username("heewon").email("h@e.com").profileUrl("p").build())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pageInfo.pageNum", is(2)))
                .andExpect(jsonPath("$.data.pageInfo.limit", is(5)));
    }


    private static final String BASE = "/scraps";

    @Test
    @DisplayName("POST /scraps/{exhibitionId} -> 스크랩 추가: 204 & 서비스 호출")
    void addScrap_204_and_serviceCalled() throws Exception {
        long exId = 10L;

        mockMvc.perform(post(BASE + "/{exhibitionId}", exId))
                .andExpect(status().isNoContent());

        // memberId는 ArgResolver에서 1L로 주입됨
        verify(scrapService).addScrap(1L, exId);
    }

    @Test
    @DisplayName("DELETE /scraps/{exhibitionId} -> 스크랩 취소(Idempotent): 204 & 서비스 호출")
    void removeScrap_204_and_serviceCalled() throws Exception {
        long exId = 10L;

        mockMvc.perform(delete(BASE + "/{exhibitionId}", exId))
                .andExpect(status().isNoContent());

        verify(scrapService).removeScrap(1L, exId);
    }
}
