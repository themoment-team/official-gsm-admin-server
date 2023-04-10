package com.themoment.officialgsm.domain.Admin.repository;

import com.themoment.officialgsm.domain.Admin.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findRefreshTokenByUserId(String userId);
}
