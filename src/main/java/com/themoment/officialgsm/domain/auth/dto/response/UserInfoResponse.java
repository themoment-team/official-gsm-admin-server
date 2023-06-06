package com.themoment.officialgsm.domain.auth.dto.response;


import com.themoment.officialgsm.domain.auth.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserInfoResponse {
    private String userName;
    private String userEmail;
    private Role role;
}
