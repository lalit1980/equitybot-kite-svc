package com.equitybot.trade.db.mongodb.property.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class PropertyRepositoryImpl implements PropertyRespositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;
	

	public UpdateResult updatePropertyByUserId(String userId, String requestToken, String access_token, String public_token) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update();
		update.set("requestToken", requestToken);
		update.set("public_token", public_token);
		update.set("access_token", access_token);
		
		return mongoTemplate.updateFirst(query, update, KiteProperty.class);
	}

	public KiteProperty findByUserId(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		KiteProperty list=mongoTemplate.findOne(query, KiteProperty.class);
		return list;
	}

	public DeleteResult deleteByUserId(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		KiteProperty kiteProperty = mongoTemplate.find(query, KiteProperty.class).get(0);
		return mongoTemplate.remove(query, kiteProperty.getClass());
	}

	@Override
	public UpdateResult updatePropertyByUserIdMapData(String userId, Map<String, String> secretQuestions) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update();
		update.set("secretQuestions", secretQuestions);
		return mongoTemplate.updateFirst(query, update, KiteProperty.class);
	}

	@Override
	public UpdateResult updatRequestTokenByUserId(String userId, String requestToken) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update();
		update.set("requestToken", requestToken);
		return mongoTemplate.updateFirst(query, update, KiteProperty.class);
		
	}

	
}
