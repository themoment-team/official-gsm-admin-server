package com.themoment.officialgsm.global.filter;

import com.themoment.officialgsm.domain.auth.entity.token.BlackList;
import com.themoment.officialgsm.domain.auth.repository.BlackListRepository;
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
import org.springframework.beans.factory.annotation.Value;
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
    private final BlackListRepository blackListRepository;
    private final JwtTokenProvider jwtProvider;

    @Value("${jwt.accessSecret}")
    private String accessSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = CookieUtil.getCookieValue(request, ConstantsUtil.accessToken);
        String oauthId = jwtProvider.getTokenOauthId(token, accessSecret);
        BlackList blackListToken = blackListRepository.findById(oauthId)
                .orElseThrow(()-> new CustomException("엑세스토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        if (token != null){
            if (token.equals(blackListToken.getAccessToken())){
                throw new CustomException("유효하지 않은 토큰입니다.", HttpStatus.CONFLICT);
            }
            UsernamePasswordAuthenticationToken auth = jwtProvider.authentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
