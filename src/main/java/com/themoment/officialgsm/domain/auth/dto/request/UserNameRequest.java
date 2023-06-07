package com.themoment.officialgsm.domain.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNameRequest {
    @Pattern(regexp = "^.{2,5}$")
    private String userName;
}
