package com.example.backend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${app.jwtExpirationMs:86400000}") // 24 hours
    private int jwtExpirationMs;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // Generate a secure key for HS512 algorithm
        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        System.out.println("=== JWT Utils Initialized ===");
        System.out.println("Generated secure 512-bit signing key for HS512 algorithm");
        System.out.println("Key size: " + (signingKey.getEncoded().length * 8) + " bits");
        System.out.println("Token expiration: " + (jwtExpirationMs / 1000 / 60 / 60) + " hours");
        System.out.println("Note: Key is generated at startup and valid for this session only");
        System.out.println("============================");
    }

    public String generateJwtToken(String username) {
        return generateTokenFromUsername(username);
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("JWT token validation failed: " + e.getMessage());
        }

        return false;
    }

    // Optional: Method to get key info for debugging/monitoring
    public String getKeyInfo() {
        return "HS512 key with " + (signingKey.getEncoded().length * 8) + " bits";
    }
}