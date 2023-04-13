package com.themoment.officialgsm.domain.Admin.service;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void signUp(SignUpRequest signUpRequest, HttpServletRequest request);

    TokenResponse signIn(SignInRequest signInRequest, HttpServletResponse httpServletResponse);

    void logout(String accessToken);

    TokenResponse tokenReissue(HttpServletRequest request, HttpServletResponse response);
}
