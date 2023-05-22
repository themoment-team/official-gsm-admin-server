package com.themoment.officialgsm.domain.User.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {
    private String userId;
    private String userPwd;
}
