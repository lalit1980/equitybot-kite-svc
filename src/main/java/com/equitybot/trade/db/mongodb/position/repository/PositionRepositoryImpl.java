package com.equitybot.trade.db.mongodb.position.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.equitybot.trade.db.mongodb.order.domain.Position;

public class PositionRepositoryImpl implements PositionCustom {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<Position> findAllByUserId(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		List<Position> list=mongoTemplate.find(query, Position.class);
		return list;
	}

	@Override
	public List<Position> findAllByTradingSymbol(String tradingSymbol) {
		Query query = new Query(Criteria.where("tradingSymbol").is(tradingSymbol));
		List<Position> list=mongoTemplate.find(query, Position.class);
		return list;
	}

}
