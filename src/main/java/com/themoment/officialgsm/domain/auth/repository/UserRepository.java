package com.themoment.officialgsm.domain.auth.repository;

import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByOauthId(String userId);

    Boolean existsByUserId(String userId);

    List<User> findUsersByRole(Role role);

    Optional<User> findByOauthId(String oauthId);
}
