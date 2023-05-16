package com.themoment.officialgsm.domain.User.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class UnapprovedUserListResponse {
    private final List<UnapprovedUserResponse> list;
}
