package com.themoment.officialgsm.global.filter;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = CookieUtil.getCookieValue(request, ConstantsUtil.accessToken);
        if (token != null){
            if (redisTemplate.opsForValue().get(token) != null){
                throw new CustomException("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            UsernamePasswordAuthenticationToken auth = jwtProvider.authentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
