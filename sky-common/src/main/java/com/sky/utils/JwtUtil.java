package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final long ADMIN_EXPIRATION = 7200000L;
    private static final long USER_EXPIRATION = 2592000000L;

    public static String createAdminToken(String secretKey, Long empId) {
        return Jwts.builder()
                .claim("empId", empId)
                .subject(String.valueOf(empId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ADMIN_EXPIRATION))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public static String createUserToken(String secretKey, Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + USER_EXPIRATION))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public static Claims parseToken(String secretKey, String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
