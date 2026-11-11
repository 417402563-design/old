package com.antifraud.util;

import com.antifraud.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CLAIM_USER_ID, userId);
        claims.put(Constants.CLAIM_ROLE, role);
        return createToken(claims, accessTokenExpiration);
    }

    public String generateRefreshToken(Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CLAIM_USER_ID, userId);
        claims.put(Constants.CLAIM_ROLE, role);
        claims.put("type", "refresh");
        return createToken(claims, refreshTokenExpiration);
    }

    private String createToken(Map<String, Object> claims, Long expiration) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get(Constants.CLAIM_USER_ID, Number.class).longValue();
    }

    public String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get(Constants.CLAIM_ROLE, String.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
