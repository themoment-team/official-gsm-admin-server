package com.themoment.officialgsm.global.util;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtil {

    private final UserRepository adminRepository;

    public User CurrentUser(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminRepository.findUserByUserId(userId)
                .orElseThrow(()-> new RuntimeException("유저가 없습니다."));
    }
}
