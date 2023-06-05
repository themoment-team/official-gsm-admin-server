package com.themoment.officialgsm.domain.auth.dto.response;


import com.themoment.officialgsm.domain.auth.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private String userName;
    private Role role;
}
