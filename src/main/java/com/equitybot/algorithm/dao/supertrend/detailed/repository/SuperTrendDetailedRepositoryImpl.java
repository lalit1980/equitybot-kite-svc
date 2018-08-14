package com.equitybot.algorithm.dao.supertrend.detailed.repository;

import com.equitybot.algorithm.dao.supertrend.detailed.domain.SuperTrendDetailedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class SuperTrendDetailedRepositoryImpl implements SuperTrendDetailedRepositoryCustom {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<SuperTrendDetailedEntity> findByInstrumentToken(long instrument) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "transactionTime"));
        query.addCriteria(Criteria.where("instrumentToken").is(instrument));
        return mongoTemplate.find(query, SuperTrendDetailedEntity.class);
    }

    @Override
    public void saveSuperTrendDetailedEntity(SuperTrendDetailedEntity superTrendDetailedEntity) {
        mongoTemplate.save(superTrendDetailedEntity);

    }

}
