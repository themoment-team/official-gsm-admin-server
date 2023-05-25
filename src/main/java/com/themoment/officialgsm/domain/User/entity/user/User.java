package com.themoment.officialgsm.domain.User.entity.user;

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

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userPwd;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "grantor_seq", referencedColumnName = "user_seq")
    private User grantor;

    private LocalDateTime approvedAt;

    @PrePersist
    public void prePersist(){
        this.role = this.role == null ? Role.UNAPPROVED : Role.ADMIN;
    }

    public void updateRoleAndGrantor(User grantor, Role role, LocalDateTime approvedAt) {
        this.grantor = grantor;
        this.role = role;
        this.approvedAt = approvedAt;
    }
}