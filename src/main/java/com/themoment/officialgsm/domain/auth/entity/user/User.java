package com.themoment.officialgsm.domain.auth.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    private String oauthId;

    private String userName;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "grantor_seq", referencedColumnName = "user_seq")
    private User grantor;

    private LocalDateTime approvedAt;

    private String requestedAt;

    public User(String oauthId, String email, Role unapproved, String requestedAt) {
        this.oauthId = oauthId;
        this.userEmail = email;
        this.role = unapproved;
        this.requestedAt = requestedAt;
    }

    public void updateRoleAndGrantor(User grantor, Role role, LocalDateTime approvedAt) {
        this.grantor = grantor;
        this.role = role;
        this.approvedAt = approvedAt;
    }

    public void updateName(String userName){
        this.userName = userName;
    }

    public User updateEmail(String email) {
        this.userEmail = email;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
