package com.typemaster.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility component for generating, signing, parsing, and validating JWT authentication tokens.
 */
@Component
public class JwtTokenProvider {

    private static final String SECRET = "TypeMasterSecureJWTSecretKey2026SuperSecretKeyHMAC256Bits";
    private static final long EXPIRATION_MS = 86400000L; // 24 Hours

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Generates a signed JWT token containing subject username, userId, and email.
     */
    public String generateToken(String username, String email, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts username subject from signed JWT token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Validates JWT token signature and expiration.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
