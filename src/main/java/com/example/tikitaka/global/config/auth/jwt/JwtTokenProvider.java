
package com.example.tikitaka.global.config.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret-key}")   // 최소 256-bit 권장. Base64로 넣는 걸 추천합니다.
    private String secret;

    @Value("${spring.jwt.expiration-ms:1800000}") // 기본 30분(ms). application.properties에서 설정 가능
    private long expMillis;

    private SecretKey key;

    // 1시간
    private final long ACCESS_TOKEN_VALIDITY_MS = 60 * 60 * 1000L;
    // 14일
    private final long REFRESH_TOKEN_VALIDITY_MS = 14L * 24 * 60 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        // secret이 Base64라면:
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            // Base64가 아니라면 그대로 bytes 사용 (충분히 긴 키 문자열 필요: 32바이트 이상 권장)
            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    // === 토큰 생성 ===
    public String createAccessToken(Long memberId) {
        return createToken(memberId, "access", ACCESS_TOKEN_VALIDITY_MS);
    }

    public String createRefreshToken(Long memberId) {
        return createToken(memberId, "refresh", REFRESH_TOKEN_VALIDITY_MS);
    }

    private String createToken(Long memberId, String type, long validityMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))  // sub = 우리 서비스의 사용자 PK
                .claim("type", type)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }


    // === 인증 객체 생성 (DB 조회 없이 클레임으로) ===
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Long memberId = Long.valueOf(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    // === 토큰에서 sub(=memberId) 가져오기 ===
    public Long getMemberId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    // === 유효성 검사 ===
    public boolean validateAccessToken(String token) {
        return validateToken(token, "access");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, "refresh");
    }

    private boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("type", String.class);
            return expectedType.equals(type) && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // === Authorization 헤더에서 Bearer 토큰 추출 ===
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        // cookie fallback
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("ACCESS_TOKEN".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }


    // === 내부 유틸 ===
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
