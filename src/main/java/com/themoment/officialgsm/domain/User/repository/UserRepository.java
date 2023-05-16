package com.themoment.officialgsm.domain.User.repository;

import com.themoment.officialgsm.domain.User.entity.user.Role;
import com.themoment.officialgsm.domain.User.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserId(String userId);

    Boolean existsByUserId(String userId);

    List<User> findUsersByRole(Role role);
}
