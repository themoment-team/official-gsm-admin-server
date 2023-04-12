package com.themoment.officialgsm.domain.Admin.service;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.SignInResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest, HttpServletRequest request);

    SignInResponse signIn(SignInRequest signInRequest);

    void logout(String accessToken);
}
