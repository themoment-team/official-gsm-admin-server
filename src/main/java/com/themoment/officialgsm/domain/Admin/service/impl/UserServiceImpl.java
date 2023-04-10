package com.themoment.officialgsm.domain.Admin.service.impl;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.Admin.service.UserService;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpRequest signUpRequest, String ip) {
        String schoolIp = "210.218.52.13";
        if (userRepository.existsByUserId(signUpRequest.getUserId())){
            throw new CustomException(ErrorCode.USERID_ALREADY_EXIST);
        }
        if (!ip.equals(schoolIp)) {
            throw new CustomException(ErrorCode.WRONG_SCHOOL_IP);
        }
        User user = User.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(signUpRequest.getPassword())
                .userName(signUpRequest.getName())
                .build();
        userRepository.save(user);
    }
}
