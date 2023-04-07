package com.maik.ApiPostgresTemplate.config;

import com.maik.ApiPostgresTemplate.domain.entities.User;
import com.maik.ApiPostgresTemplate.exceptions.ApiException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    private static final long ACCESS_TOKEN_DURATION = 2 * 60 * 60 * 1000; // 2 hour
    private static final long REFRESH_TOKEN_DURATION = 10 * 24 * 60 * 60 * 1000; // 10 days

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String appName;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer(appName)
                .claim("roles", user.getRoles().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer(appName)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw ApiException.builder()
                    .code(401)
                    .message("JWT expired")
                    .build();
        } catch (IllegalArgumentException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("Token is null, empty or only whitespace")
                    .build();
        } catch (MalformedJwtException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("JWT is invalid")
                    .build();
        } catch (UnsupportedJwtException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("JWT is not supported")
                    .build();
        } catch (SignatureException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("Signature validation failed")
                    .build();
        }
        return true;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).requireIssuer(appName)
                    .require("type", "refresh")
                    .parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException ex) {;
            throw ApiException.builder()
                    .code(401)
                    .message("Refresh token expired")
                    .build();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("Invalid refresh token")
                    .build();
        } catch (JwtException ex) {
            throw ApiException.builder()
                    .code(400)
                    .message("Invalid refresh token")
                    .build();
        }
        return true;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
