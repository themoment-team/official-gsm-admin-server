package com.themoment.officialgsm.domain.auth.controller;

import com.themoment.officialgsm.domain.auth.dto.request.UserNameRequest;
import com.themoment.officialgsm.domain.auth.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.auth.dto.response.UserInfoResponse;
import com.themoment.officialgsm.domain.auth.service.GrantorService;
import com.themoment.officialgsm.domain.auth.service.UserService;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GrantorService grantorService;

    @PatchMapping("/username")
    public ResponseEntity<Void> nameSet(@Valid @RequestBody UserNameRequest request){
        userService.nameSetExecute(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoResponse> userInfo(){
        UserInfoResponse userinfo = userService.userInfoExecute();
        return new ResponseEntity<>(userinfo, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        String accessToken = CookieUtil.getCookieValue(request, ConstantsUtil.accessToken);
        userService.logout(accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/token/reissue")
    public ResponseEntity<Void> tokenReissue(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtil.getCookieValue(request, ConstantsUtil.refreshToken);
        userService.tokenReissue(token, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unapproved/list")
    public ResponseEntity<List<UnapprovedUserResponse>> unapprovedList(){
        List<UnapprovedUserResponse> list = grantorService.unapprovedListExecute();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/approved/{userSeq}")
    public ResponseEntity<Void> approved(@PathVariable Long userSeq){
        grantorService.approvedExecute(userSeq);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/approved/{userSeq}")
    public ResponseEntity<Void> refuseApproved(@PathVariable Long userSeq){
        grantorService.refuseApprovedExecute(userSeq);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
