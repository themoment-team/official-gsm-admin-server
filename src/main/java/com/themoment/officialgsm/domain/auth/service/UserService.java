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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final RedisTemplate redisTemplate;
    private final UserUtil userUtil;

    @Value("${ip}")
    private String schoolIp;

    @Transactional
    public void signUp(SignUpRequest signUpRequest, String ip) {
        if (userRepository.existsByUserId(signUpRequest.getUserId())){
            throw new CustomException("이미 사용되고 있는 유저 아이디입니다.", HttpStatus.CONFLICT);
        }
        if (!ip.equals(schoolIp)) {
            throw new CustomException("학교 아이피가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(passwordEncoder.encode(signUpRequest.getUserPwd()))
                .userName(signUpRequest.getUserName())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest, HttpServletResponse response) {
        User user = userRepository.findUserByUserId(signInRequest.getUserId())
                .orElseThrow(() -> new CustomException("사용자 아이디를 찾지 못하였습니다.", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(signInRequest.getUserPwd(), user.getUserPwd())){
            throw new CustomException("패스워드가 틀렸습니다.", HttpStatus.BAD_REQUEST);
        }

        String accessToken = jwtTokenProvider.generatedAccessToken(signInRequest.getUserId());
        String refreshToken = jwtTokenProvider.generatedRefreshToken(signInRequest.getUserId());
        CookieUtil.addTokenCookie(response, ConstantsUtil.accessToken, accessToken, jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(), true);
        CookieUtil.addTokenCookie(response, ConstantsUtil.refreshToken, refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME(), true);
        RefreshToken entityToRedis =  new RefreshToken(signInRequest.getUserId(),refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME());
        refreshTokenRepository.save(entityToRedis);
        return TokenResponse.builder()
                .expiredAt(jwtTokenProvider.getExpiredAtToken())
                .build();
    }

    @Transactional
    public TokenResponse tokenReissue(String token, HttpServletResponse response) {
        String secret = jwtTokenProvider.getRefreshSecret();
        String userId = jwtTokenProvider.getTokenUserId(token, secret);
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new CustomException("리프레시 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST));
        String newAccessToken = jwtTokenProvider.generatedAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generatedRefreshToken(userId);

        if (!refreshToken.getToken().equals(token) && !jwtTokenProvider.isValidToken(token, secret)) {
            throw new CustomException("리프레시 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        CookieUtil.addTokenCookie(response, ConstantsUtil.accessToken, newAccessToken, jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(), true);
        CookieUtil.addTokenCookie(response, ConstantsUtil.refreshToken, newRefreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME(), true);
        refreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);
        return TokenResponse.builder()
                .expiredAt(jwtTokenProvider.getExpiredAtToken())
                .build();
    }

    @Transactional
    public void logout(String accessToken) {
        User user = userUtil.getCurrentUser();
        RefreshToken refreshToken = refreshTokenRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException("리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
        saveBlackList(user.getUserId(), accessToken);
    }

    private void saveBlackList(String userId, String accessToken){
        if(redisTemplate.opsForValue().get(accessToken) != null){
            throw new CustomException("이미 블랙리스트에 존재합니다.", HttpStatus.CONFLICT);
        }
        BlackList blackList = BlackList.builder()
                .userId(userId)
                .accessToken(accessToken)
                .timeToLive(jwtTokenProvider.getExpiredAtoTokenToLong())
                .build();
        blackListRepository.save(blackList);
    }
}
