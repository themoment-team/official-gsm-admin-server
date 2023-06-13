package com.themoment.officialgsm.domain.auth.controller;

import com.themoment.officialgsm.domain.auth.dto.request.UserNameRequest;
import com.themoment.officialgsm.domain.auth.dto.response.UnapprovedUserResponse;
import com.themoment.officialgsm.domain.auth.dto.response.UserInfoResponse;
import com.themoment.officialgsm.domain.auth.service.GrantorService;
import com.themoment.officialgsm.domain.auth.service.UserService;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GrantorService grantorService;

    @PatchMapping("/username")
    @Operation(summary = "이름 등록 요청", description = "oauth로그인 후 이름을 등록하는 api", tags = {"User Controller"})
    public ResponseEntity<Void> nameSet(@Valid @RequestBody UserNameRequest request){
        userService.nameSetExecute(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/userinfo")
    @Operation(summary = "회원정보 조회 요청", description = "회원정보를 조회하는 api", tags = {"User Controller"})
    public ResponseEntity<UserInfoResponse> userInfo(){
        UserInfoResponse userinfo = userService.userInfoExecute();
        return new ResponseEntity<>(userinfo, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "로그아웃하는 api", tags = {"User Controller"})
    public ResponseEntity<Void> logout(HttpServletRequest request){
        String accessToken = CookieUtil.getCookieValue(request, ConstantsUtil.accessToken);
        userService.logout(accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/token/reissue")
    @Operation(summary = "토큰 재발급 요청", description = "토큰을 재발급하는 api", tags = {"User Controller"})
    public ResponseEntity<Void> tokenReissue(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtil.getCookieValue(request, ConstantsUtil.refreshToken);
        userService.tokenReissue(token, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unapproved/list")
    @Operation(summary = "승인대기 회원정보 조회 요청", description = "승인대기중인 회원정보를 조회하는 api", tags = {"User Controller"})
    public ResponseEntity<List<UnapprovedUserResponse>> unapprovedList(){
        List<UnapprovedUserResponse> list = grantorService.unapprovedListExecute();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/approved/{userSeq}")
    @Operation(summary = "회원을 승인하는 요청", description = "승인대기중인 회원을 승인하는 api", tags = {"User Controller"})
    public ResponseEntity<Void> approved(@PathVariable Long userSeq){
        grantorService.approvedExecute(userSeq);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/approved/{userSeq}")
    @Operation(summary = "회원 승인을 거절하는 요청", description = "승인대기중인 회원을 승인 거절하는 api", tags = {"User Controller"})
    public ResponseEntity<Void> refuseApproved(@PathVariable Long userSeq){
        grantorService.refuseApprovedExecute(userSeq);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
