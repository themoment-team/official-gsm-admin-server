package com.themoment.officialgsm.domain.User.repository;

import com.themoment.officialgsm.domain.User.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
