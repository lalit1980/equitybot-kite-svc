package com.equitybot.trade.db.mongodb.order.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.equitybot.trade.db.mongodb.order.domain.TrailStopLossResponse;

public class TrailStopLossRepositoryImpl implements TrailStopLossRespositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public TrailStopLossResponse findByOrderId(String orderId) {
		Query query = new Query(Criteria.where("orderId").is(orderId));
		return mongoTemplate.findOne(query, TrailStopLossResponse.class);
	}

	@Override
	public List<TrailStopLossResponse> findAllByUserId(String userId) {
		Query query = new Query(Criteria.where("orderId").is(userId));
		List<TrailStopLossResponse> list=mongoTemplate.find(query, TrailStopLossResponse.class);
		return list;
	}

	@Override
	public List<TrailStopLossResponse> findAllByTradingSymbol(String tradingSymbol) {
		Query query = new Query(Criteria.where("tradingSymbol").is(tradingSymbol));
		List<TrailStopLossResponse> list=mongoTemplate.find(query, TrailStopLossResponse.class);
		return list;
	}

	@Override
	public List<TrailStopLossResponse> findAllByOrderStatus(String orderStatus) {
		Query query = new Query(Criteria.where("status").is(orderStatus));
		List<TrailStopLossResponse> list=mongoTemplate.find(query, TrailStopLossResponse.class);
		return list;
	}

	@Override
	public List<TrailStopLossResponse> findAllByTransactionType(String transactionType) {
		Query query = new Query(Criteria.where("transactionType").is(transactionType));
		List<TrailStopLossResponse> list=mongoTemplate.find(query, TrailStopLossResponse.class);
		return list;
	}

	@Override
	public List<TrailStopLossResponse> findAllByInstrumentToken(String instrumentToken) {
		Query query = new Query(Criteria.where("instrumentToken").is(instrumentToken));
		List<TrailStopLossResponse> list=mongoTemplate.find(query, TrailStopLossResponse.class);
		return list;
	}
}
