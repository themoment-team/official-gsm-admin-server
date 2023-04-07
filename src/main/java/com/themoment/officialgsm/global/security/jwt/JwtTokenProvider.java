package com.themoment.officialgsm.global.security.jwt;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    @AllArgsConstructor
    public enum TokenType{
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    private Key getSignInKey(String secretKey){
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateToken(String userId, String type, String secret, long expiredTime){
        final Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("type", type);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(secret), SignatureAlgorithm.ES256)
                .compact();
    }

    private Claims getTokenBody(String token, String secret){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            throw new CustomException(ErrorCode.TOKEN_EXPIRATION);
        } catch (JwtException e){
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
    }
}
