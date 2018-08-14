package com.equitybot.algorithm.dao.supertrend.profit.repository;

import com.equitybot.algorithm.dao.supertrend.profit.domain.SuperTrendProfitEntity;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class SuperTrendProfitRepositoryImpl implements SuperTrendProfitRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    public void saveData(SuperTrendProfitEntity superTrendProfitEntity) {
        mongoTemplate.save(superTrendProfitEntity);
    }

    @Override
    public long updateByInstrument(SuperTrendProfitEntity superTrendProfitEntity) {
        Query query = new Query(Criteria.where("instrumentToken").is(superTrendProfitEntity.getInstrumentToken()));
        Update update = new Update();
        update.set("totalProfitLoss", superTrendProfitEntity.getTotalProfitLoss());
        UpdateResult result = mongoTemplate.updateMulti(query, update, SuperTrendProfitEntity.class);
        if (result != null) {
            return result.getModifiedCount();
        } else {
            return 0;
        }
    }

    @Override
    public void saveUpdate(SuperTrendProfitEntity superTrendProfitEntity) {
        if (updateByInstrument(superTrendProfitEntity) == 0) {
            saveData(superTrendProfitEntity);
        }
    }

    @Override
    public long deleteByInstrumentToken(SuperTrendProfitEntity superTrendProfitEntity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("instrumentToken").is(superTrendProfitEntity.getInstrumentToken()));
        DeleteResult result = mongoTemplate.remove(query, SuperTrendProfitEntity.class);
        if (result != null) {
            return result.getDeletedCount();
        } else {
            return 0;
        }
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), "superTrendProfitEntity");
    }

    @Override
    public List<SuperTrendProfitEntity> findByInstrumentToken(long token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("instrumentToken").is(token));
        return mongoTemplate.find(query, SuperTrendProfitEntity.class);
    }

    @Override
    public List<SuperTrendProfitEntity> getMexProfitDataList(int numOfMexRecord) {
        Query query = new Query();
        query.limit(numOfMexRecord);
        query.with(new Sort(Sort.Direction.DESC, "totalProfitLoss"));
        return mongoTemplate.find(query, SuperTrendProfitEntity.class);
    }

    @Override
    public List<SuperTrendProfitEntity> getMexLossDataList(int numOfMenRecord) {
        Query query = new Query();
        query.limit(numOfMenRecord);
        query.with(new Sort(Sort.Direction.ASC, "totalProfitLoss"));
        return mongoTemplate.find(query, SuperTrendProfitEntity.class);
    }

}
