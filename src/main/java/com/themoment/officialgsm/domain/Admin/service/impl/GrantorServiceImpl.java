package com.themoment.officialgsm.domain.Admin.service.impl;

import com.themoment.officialgsm.domain.Admin.entity.Role;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.Admin.repository.UserRepository;
import com.themoment.officialgsm.domain.Admin.service.GrantorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantorServiceImpl implements GrantorService {
    private final UserRepository userRepository;

    @Override
    public List<UnapprovedUserResponse> unapprovedListExecute() {
        return userRepository.findUsersByRole(Role.UNAPPROVED).stream()
                .map(user -> new UnapprovedUserResponse(
                        user.getUserSeq(),
                        user.getUserId(),
                        user.getUserName(),
                        user.getRole()
                )).collect(Collectors.toList());
    }
}
