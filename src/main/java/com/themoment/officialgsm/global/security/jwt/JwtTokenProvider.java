package com.themoment.officialgsm.global.security.jwt;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.security.auth.AuthDetailsService;
import com.themoment.officialgsm.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final AuthDetailsService authDetailsService;

    private final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 120 * 1000;

    private final long REFRESH_TOKEN_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_TIME * 12;


    @AllArgsConstructor
    public enum TokenType{
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    private Key getSignInKey(String secretKey){
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generatedToken(String userId, String type, String secret, long expiredTime){
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

    public String generatedAccessToken(String userId){
        return generatedToken(userId, TokenType.ACCESS_TOKEN.name(), jwtProperties.getAccessSecret(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generatedRefreshToken(String userId){
        return generatedToken(userId, TokenType.REFRESH_TOKEN.name(), jwtProperties.getRefreshSecret(), REFRESH_TOKEN_EXPIRE_TIME);
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

    public String validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return "";
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return validateToken(token);
    }

    public UsernamePasswordAuthenticationToken authentication(String token){
        UserDetails userDetails = authDetailsService.loadUserByUsername(getTokenUserId(token, jwtProperties.getAccessSecret()));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getTokenUserId(String token, String secret){
        return getTokenBody(token, secret).get("userId", String.class);
    }

    public ZonedDateTime getExpiredAtToken(){
        return ZonedDateTime.now().plusSeconds(ACCESS_TOKEN_EXPIRE_TIME);
    }

    public long getExpiredAtoTokenToLong(){
        return ACCESS_TOKEN_EXPIRE_TIME/1000L;
    }

}
