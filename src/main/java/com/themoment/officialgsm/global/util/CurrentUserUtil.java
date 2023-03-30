package com.themoment.officialgsm.global.util;

import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository adminRepository;

    public User CurrentUser(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminRepository.findUserByName(id)
                .orElseThrow(()-> new RuntimeException("유저가 없습니다."));
    }
}
