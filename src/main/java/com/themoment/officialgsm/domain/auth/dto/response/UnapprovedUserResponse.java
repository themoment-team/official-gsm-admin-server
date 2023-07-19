package com.themoment.officialgsm.domain.auth.dto.response;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnapprovedUserResponse {
    private Long userSeq;
    private String userName;
    private Role role;
    private String requestedAt;
}
