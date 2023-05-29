package com.themoment.officialgsm.global.util;

import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User getCurrentUser(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new CustomException("요청하신 사용자 id:{}가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
