package com.themoment.officialgsm.domain.Admin.service.impl;

import com.themoment.officialgsm.domain.Admin.entity.Role;
import com.themoment.officialgsm.domain.Admin.entity.User;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.Admin.service.GrantorService;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantorServiceImpl implements GrantorService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    @Override
    public List<UnapprovedUserResponse> unapprovedListExecute() {
        return userRepository.findUsersByRole(Role.UNAPPROVED).stream()
                .map(user -> new UnapprovedUserResponse(
                        user.getUserSeq(),
                        user.getUserId().substring(0,4) + "****",
                        user.getUserName(),
                        user.getRole()
                )).collect(Collectors.toList());
    }

    @Override
    public void approvedExecute(Long id) {
        User grantor = userUtil.CurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException("Id를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        LocalDateTime approvedAt = LocalDateTime.now();
        user.updateRoleAndGrantor(grantor, Role.ADMIN, approvedAt);
        userRepository.save(user);
    }
}
