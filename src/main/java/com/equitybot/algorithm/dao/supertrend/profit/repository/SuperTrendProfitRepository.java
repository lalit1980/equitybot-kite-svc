package com.equitybot.algorithm.dao.supertrend.profit.repository;

import com.equitybot.algorithm.dao.supertrend.profit.domain.SuperTrendProfitEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SuperTrendProfitRepository extends MongoRepository<SuperTrendProfitEntity, Long>, SuperTrendProfitRepositoryCustom {

}
