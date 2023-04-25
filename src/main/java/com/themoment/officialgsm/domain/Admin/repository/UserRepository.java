package com.themoment.officialgsm.domain.Admin.repository;

import com.themoment.officialgsm.domain.Admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);
}
