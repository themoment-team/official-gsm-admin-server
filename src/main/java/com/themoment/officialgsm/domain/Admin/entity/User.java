package com.themoment.officialgsm.domain.Admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userSeq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPwd;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user")
    @JoinColumn(name = "approver")
    private Approver approver;

    @PrePersist
    public void prePersist(){
        this.role = this.role == null ? Role.UN_APPROVE : Role.ADMIN;
    }
}
