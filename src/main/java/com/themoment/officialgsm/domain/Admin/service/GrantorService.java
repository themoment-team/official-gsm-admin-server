package com.themoment.officialgsm.domain.Admin.service;

import com.themoment.officialgsm.domain.Admin.presentation.dto.response.UnapprovedUserResponse;

import java.util.List;

public interface GrantorService {
    List<UnapprovedUserResponse> unapprovedListExecute();

    void approvedExecute(Long id);
}
