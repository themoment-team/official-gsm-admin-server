package com.themoment.officialgsm.domain.popup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AddPopupRequest {

    private String link;

    private LocalDateTime expirationDateTime;
}
