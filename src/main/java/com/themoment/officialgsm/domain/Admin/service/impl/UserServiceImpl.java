package com.themoment.officialgsm.domain.Admin.service.impl;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.SignInResponse;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.Admin.service.UserService;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${ip}")
    private final String schoolIp;

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

    }
}
