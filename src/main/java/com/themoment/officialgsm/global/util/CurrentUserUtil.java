package com.themoment.officialgsm.global.util;

import com.themoment.officialgsm.domain.Admin.entity.Admin;
import com.themoment.officialgsm.domain.Admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final AdminRepository adminRepository;

    public Admin CurrentUser(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminRepository.findAdminByName(id)
                .orElseThrow(()-> new RuntimeException("유저가 없습니다."));
    }
}
