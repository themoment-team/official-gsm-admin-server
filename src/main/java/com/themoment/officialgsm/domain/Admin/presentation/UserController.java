package com.themoment.officialgsm.domain.Admin.presentation;

import com.themoment.officialgsm.domain.Admin.presentation.dto.request.SignUpRequest;
import com.themoment.officialgsm.domain.Admin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletRequest request){
        String ip = request.getRemoteAddr();
        userService.signUp(signUpRequest, ip);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
