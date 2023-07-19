package com.themoment.officialgsm.domain.auth.dto;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class UserProfile {
    private final String oauthId;
    private final String email;

    public User toEntity() {
        LocalDate requestedAt = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = requestedAt.format(formatter);
        return new User(oauthId, email, Role.UNAPPROVED, formattedDate);
    }
}
