package com.example.tikitaka.global.config;

import com.example.tikitaka.global.config.auth.CustomAuthenticationEntryPoint;
import com.example.tikitaka.global.config.auth.CustomOAuth2MemberService;
import org.springframework.beans.factory.annotation.Value;
import com.example.tikitaka.global.config.auth.OAuth2AuthenticationSuccessHandler;
import com.example.tikitaka.global.config.auth.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${server.server-url}")
    private String SERVER_URL;

    @Value("${server.front-urls}")
    private String[] FRONT_URLS;


    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint; // ✅ 추가




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final String[] SwaggerPatterns = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/images"
    };

    private final String[] SecurityPatterns = {
            "/signup", "/", "/login", "/Oauth2/**", "/oauth2/**", "/login/oauth2/**", "/api/auth/**", "/error"
    };

    private final String[] ActuatorPatterns = {
            "/actuator/health"
    };

    private final String[] GetPermittedPatterns = {
            "/exhibition/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SwaggerPatterns)
                        .permitAll()
                        .requestMatchers(SecurityPatterns)
                        .permitAll()
                        .requestMatchers(ActuatorPatterns)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, GetPermittedPatterns)
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 지정하지 않은 api는 403
                )
                // OAuth2 로그인: 사용자 정보 서비스 + 성공 핸들러(JWT 발급/리다이렉트)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.userService(customOAuth2MemberService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(java.util.List.of(
                "http://localhost:3000",
                "http://localhost:8080",
                SERVER_URL

        ));
        cfg.getAllowedOrigins().addAll(Arrays.asList(FRONT_URLS));
        // 브라우저가 보낼/보려는 헤더를 명시 (Authorization 꼭 포함)
        cfg.setAllowedHeaders(java.util.List.of("Authorization","Content-Type","X-Requested-With"));
        cfg.setExposedHeaders(java.util.List.of("Location","Content-Disposition"));
        cfg.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(1800L);

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
