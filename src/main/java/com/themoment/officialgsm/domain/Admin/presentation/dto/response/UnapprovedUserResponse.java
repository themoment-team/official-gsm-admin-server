package com.themoment.officialgsm.domain.Admin.presentation.dto.response;

import com.themoment.officialgsm.domain.Admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnapprovedUserResponse {
    private Long userSeq;
    private String userId;
    private String userName;
    private Role role;
}
