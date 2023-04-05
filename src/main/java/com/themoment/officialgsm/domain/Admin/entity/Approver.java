package com.themoment.officialgsm.domain.Admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = true)
    private Long grantorSeq;

    @Column(nullable = true)
    private String grantorName;

    @Column(nullable = true)
    private LocalDateTime approvedAt;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "approver")
    private User user;
}
