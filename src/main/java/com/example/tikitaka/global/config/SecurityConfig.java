package com.example.tikitaka.global.config;

import com.example.tikitaka.global.config.auth.OAuth2AuthenticationSuccessHandler;
import com.example.tikitaka.global.config.auth.jwt.JwtAuthFilter;
import com.example.tikitaka.global.config.auth.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthFilter jwtAuthFilter;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final String[] SwaggerPatterns = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/images"
    };

    private final String[] SecurityPatterns = {
            "/signup", "/", "/login", "/Oauth2/**", "/oauth2/**", "/login/oauth2/**"
    };

    private final String[] ActuatorPatterns = {
            "/actuator/health"
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
                        .anyRequest().authenticated())
                // OAuth2 로그인: 사용자 정보 서비스 + 성공 핸들러(JWT 발급/리다이렉트)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
