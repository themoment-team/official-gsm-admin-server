package com.themoment.officialgsm.domain.auth.service;

import com.themoment.officialgsm.domain.auth.entity.token.BlackList;
import com.themoment.officialgsm.domain.auth.entity.token.RefreshToken;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.auth.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.auth.dto.response.TokenResponse;
import com.themoment.officialgsm.domain.auth.repository.BlackListRepository;
import com.themoment.officialgsm.domain.auth.repository.RefreshTokenRepository;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import com.themoment.officialgsm.global.util.UserUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final RedisTemplate redisTemplate;
    private final UserUtil userUtil;

    @Transactional
    public void nameSetExecute(String userName) {
        User user = userUtil.getCurrentUser();
        user.updateName(userName);
        userRepository.save(user);
    }

    @Transactional
    public void tokenReissue(String token, HttpServletResponse response) {
        String secret = jwtTokenProvider.getRefreshSecret();
        String oauthId = jwtTokenProvider.getTokenOauthId(token, secret);
        RefreshToken refreshToken = refreshTokenRepository.findById(oauthId)
                .orElseThrow(() -> new CustomException("리프레시 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST));
        String newAccessToken = jwtTokenProvider.generatedAccessToken(oauthId);
        String newRefreshToken = jwtTokenProvider.generatedRefreshToken(oauthId);

        if (!refreshToken.getToken().equals(token) && !jwtTokenProvider.isValidToken(token, secret)) {
            throw new CustomException("리프레시 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        CookieUtil.addTokenCookie(response, ConstantsUtil.accessToken, newAccessToken, jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(), true);
        CookieUtil.addTokenCookie(response, ConstantsUtil.refreshToken, newRefreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME(), true);
        refreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logout(String accessToken) {
        User user = userUtil.getCurrentUser();
        RefreshToken refreshToken = refreshTokenRepository.findById(user.getOauthId())
                .orElseThrow(() -> new CustomException("리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
        saveBlackList(user.getOauthId(), accessToken);
    }

    private void saveBlackList(String oauthId, String accessToken){
        if(redisTemplate.opsForValue().get(accessToken) != null){
            throw new CustomException("이미 블랙리스트에 존재합니다.", HttpStatus.CONFLICT);
        }
        BlackList blackList = BlackList.builder()
                .oauthId(oauthId)
                .accessToken(accessToken)
                .timeToLive(jwtTokenProvider.getExpiredAtoTokenToLong())
                .build();
        blackListRepository.save(blackList);
    }
}
