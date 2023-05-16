package com.themoment.officialgsm.domain.User.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {
    private String userId;
    private String userPwd;
}
