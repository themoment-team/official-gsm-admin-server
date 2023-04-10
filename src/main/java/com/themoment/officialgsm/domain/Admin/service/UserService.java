package com.themoment.officialgsm.domain.Admin.service;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest, String ip);
}
