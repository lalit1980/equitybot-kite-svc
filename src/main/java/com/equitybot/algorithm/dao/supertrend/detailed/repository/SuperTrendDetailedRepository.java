package com.equitybot.algorithm.dao.supertrend.detailed.repository;

import com.equitybot.algorithm.dao.supertrend.detailed.domain.SuperTrendDetailedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuperTrendDetailedRepository extends MongoRepository<SuperTrendDetailedEntity, Long>, SuperTrendDetailedRepositoryCustom {

}
