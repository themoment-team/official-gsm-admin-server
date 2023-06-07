package com.themoment.officialgsm.domain.auth.repository;

import com.themoment.officialgsm.domain.auth.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
