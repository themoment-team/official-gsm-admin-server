package com.themoment.officialgsm.domain.auth.dto;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import lombok.*;

@Getter
@Builder
public class UserProfile {
    private final String oauthId;
    private final String email;

    public User toEntity() {
        return new User(oauthId, email, Role.UNAPPROVED);
    }
}
