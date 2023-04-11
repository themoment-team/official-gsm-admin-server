package com.themoment.officialgsm.domain.Admin.service.impl;

import com.themoment.officialgsm.domain.Admin.entity.BlackList;
import com.themoment.officialgsm.domain.Admin.entity.RefreshToken;
import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.SignInResponse;
import com.themoment.officialgsm.domain.Admin.repository.BlackListRepository;
import com.themoment.officialgsm.domain.Admin.repository.RefreshTokenRepository;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.Admin.service.UserService;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import com.themoment.officialgsm.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final RedisTemplate redisTemplate;
    private final UserUtil userUtil;

    @Value("${ip}")
    private String schoolIp;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpRequest signUpRequest, String ip) {
        if (userRepository.existsByUserId(signUpRequest.getUserId())){
            throw new CustomException(ErrorCode.USERID_ALREADY_EXIST);
        }
        if (!ip.equals(schoolIp)) {
            throw new CustomException(ErrorCode.WRONG_SCHOOL_IP);
        }
        User user = User.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(passwordEncoder.encode(signUpRequest.getPassword()))
                .userName(signUpRequest.getName())
                .build();
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findUserByUserId(signInRequest.getUserId())
                .orElseThrow(()-> new CustomException(ErrorCode.USERID_NOT_FOUND));
        if (!passwordEncoder.matches(signInRequest.getUserPwd(), user.getUserPwd())){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generatedAccessToken(signInRequest.getUserId());
        String refreshToken = jwtTokenProvider.generatedRefreshToken(signInRequest.getUserId());
        RefreshToken entityToRedis = new RefreshToken(signInRequest.getUserId(), refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME());
        refreshTokenRepository.save(entityToRedis);
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredAt(jwtTokenProvider.getExpiredAtToken())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout(String accessToken) {
        User user = userUtil.CurrentUser();
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUserId(user.getUserId())
                .orElseThrow(()-> new CustomException(ErrorCode.REFRESH_TOKEN_NOTFOUND));
        refreshTokenRepository.delete(refreshToken);
        saveBlackList(user.getUserId(), accessToken);
    }

    private void saveBlackList(String userId, String accessToken){
        if(redisTemplate.opsForValue().get(accessToken) != null){
            throw new CustomException(ErrorCode.BLACK_LIST_ALREADY_EXIST);
        }
        BlackList blackList = BlackList.builder()
                .userId(userId)
                .accessToken(jwtTokenProvider.validateToken(accessToken))
                .timeToLive(jwtTokenProvider.getExpiredAtoTokenToLong())
                .build();
        blackListRepository.save(blackList);
    }
}
