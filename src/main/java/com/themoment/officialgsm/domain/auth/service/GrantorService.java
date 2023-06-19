package com.themoment.officialgsm.domain.auth.service;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantorService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public List<UnapprovedUserResponse> unapprovedListExecute() {
        return userRepository.findUsersByRole(Role.UNAPPROVED).stream()
                .map(user -> new UnapprovedUserResponse(
                        user.getUserSeq(),
                        user.getUserName(),
                        user.getRole(),
                        user.getRequestedAt()
                )).collect(Collectors.toList());
    }

    @Transactional
    public void approvedExecute(Long userSeq) {
        User grantor = userUtil.getCurrentUser();
        User user = userRepository.findById(userSeq)
                .orElseThrow(()-> new CustomException("Id를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        LocalDateTime approvedAt = LocalDateTime.now();
        user.updateRoleAndGrantor(grantor, Role.ADMIN, approvedAt);
        userRepository.save(user);
    }

    @Transactional
    public void refuseApprovedExecute(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(()-> new CustomException("Id를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }
}
