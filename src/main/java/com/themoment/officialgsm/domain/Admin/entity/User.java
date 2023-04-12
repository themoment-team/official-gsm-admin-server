package com.themoment.officialgsm.domain.Admin.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_pwd",nullable = false)
    private String userPwd;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "grantor_seq", referencedColumnName = "user_seq")
    private User grantor;

    private LocalDateTime approvedAt;

    public void setGrantor(User grantor) {
        this.grantor = grantor;
    }

    @PrePersist
    public void prePersist(){
        this.role = this.role == null ? Role.UN_APPROVE : Role.ADMIN;
    }
}
