package com.themoment.officialgsm.domain.auth.repository;

import com.themoment.officialgsm.domain.auth.entity.token.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    Optional<BlackList> findByAccessToken(String accessToken);
}
