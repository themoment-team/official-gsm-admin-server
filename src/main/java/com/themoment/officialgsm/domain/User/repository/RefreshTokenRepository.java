package com.themoment.officialgsm.domain.User.repository;

import com.themoment.officialgsm.domain.User.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}