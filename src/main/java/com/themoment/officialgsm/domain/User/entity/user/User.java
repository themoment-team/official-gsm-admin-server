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

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(name = "grantor_seq", referencedColumnName = "user_seq")
    private User grantor;

    @Column(name = "approved_at", nullable = false)
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
