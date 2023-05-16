package com.themoment.officialgsm.domain.User.service;

import com.themoment.officialgsm.domain.User.entity.Role;
import com.themoment.officialgsm.domain.User.entity.User;
import com.themoment.officialgsm.domain.User.presentation.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.User.repository.UserRepository;
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
public class GrantorService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public List<UnapprovedUserResponse> unapprovedListExecute() {
        return userRepository.findUsersByRole(Role.UNAPPROVED).stream()
                .map(user -> new UnapprovedUserResponse(
                        user.getUserSeq(),
                        user.getUserId().substring(0,4) + "****",
                        user.getUserName(),
                        user.getRole()
                )).collect(Collectors.toList());
    }

    public void approvedExecute(Long id) {
        User grantor = userUtil.getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException("Id를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        LocalDateTime approvedAt = LocalDateTime.now();
        user.updateRoleAndGrantor(grantor, Role.ADMIN, approvedAt);
        userRepository.save(user);
    }
}
