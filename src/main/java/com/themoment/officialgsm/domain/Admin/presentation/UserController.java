package com.themoment.officialgsm.domain.Admin.presentation;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignInRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.presentation.dto.response.SignInResponse;
import com.themoment.officialgsm.domain.Admin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletRequest request){
        userService.signUp(signUpRequest, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest){
        SignInResponse data = userService.signIn(signInRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization")String accessToken){
        userService.logout(accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
