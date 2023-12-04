package com.themoment.officialgsm.domain.popup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Popup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popup_seq")
    private Long popupSeq;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "link", nullable = true)
    private String link;

    @Column(name = "expiration_date_time", nullable = false)
    private LocalDateTime expirationDateTime;
}
