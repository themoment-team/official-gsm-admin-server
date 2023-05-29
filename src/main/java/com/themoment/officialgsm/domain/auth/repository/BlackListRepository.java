package com.themoment.officialgsm.domain.auth.repository;

import com.themoment.officialgsm.domain.auth.entity.token.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
