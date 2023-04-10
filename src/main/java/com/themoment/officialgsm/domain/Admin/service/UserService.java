package com.themoment.officialgsm.domain.Admin.service;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
}
