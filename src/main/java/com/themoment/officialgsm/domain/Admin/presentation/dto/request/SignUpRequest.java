package com.themoment.officialgsm.domain.Admin.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String userName;
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*(?i)[a-zA-Z])[a-zA-Z\\d]{6,20}$", message = "영문자와 숫자를 조합하여 6~20자 사이로 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]\\|:;\"'<>,.?/])(?!.*\\s).{8,20}$", message = "영문자, 숫자 그리고 특수문자(!@#$%^&*()_+-={}[]|:;\"'<>,.?/)를 조합하여 8~20자 사이로 입력해주세요.")
    private String userPwd;
}