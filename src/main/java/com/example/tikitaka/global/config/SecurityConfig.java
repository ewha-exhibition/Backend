package com.example.tikitaka.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${server.server-url}")
    private String SERVER_URL;

    @Value("${server.front-urls}")
    private String[] FRONT_URLS;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final String[] SwaggerPatterns = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/images"
    };

    private final String[] SecurityPatterns = {
            "/signup", "/", "/login", "/Oauth2/**"
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
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

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
