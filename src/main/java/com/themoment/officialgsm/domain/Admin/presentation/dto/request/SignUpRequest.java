package com.themoment.officialgsm.domain.Admin.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "[a-zA-Z]")
    private String password;
}
