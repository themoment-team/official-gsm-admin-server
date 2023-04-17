package com.themoment.officialgsm.domain.Admin.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {
    private String userId;
    private String userPwd;
}
