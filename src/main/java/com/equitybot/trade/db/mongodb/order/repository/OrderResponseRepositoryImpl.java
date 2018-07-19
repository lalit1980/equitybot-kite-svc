package com.equitybot.trade.db.mongodb.order.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.mongodb.client.result.UpdateResult;

public class OrderResponseRepositoryImpl implements OrderResponseRespositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public OrderResponse findByOrderId(String orderId) {
		Query query = new Query(Criteria.where("orderId").is(orderId));
		return mongoTemplate.findOne(query, OrderResponse.class);
	}

	@Override
	public List<OrderResponse> findAllByUserId(String userId) {
		Query query = new Query(Criteria.where("orderId").is(userId));
		List<OrderResponse> list=mongoTemplate.find(query, OrderResponse.class);
		return list;
	}

	@Override
	public List<OrderResponse> findAllByTradingSymbol(String tradingSymbol) {
		Query query = new Query(Criteria.where("tradingSymbol").is(tradingSymbol));
		List<OrderResponse> list=mongoTemplate.find(query, OrderResponse.class);
		return list;
	}

	@Override
	public List<OrderResponse> findAllByOrderStatus(String orderStatus) {
		Query query = new Query(Criteria.where("status").is(orderStatus));
		List<OrderResponse> list=mongoTemplate.find(query, OrderResponse.class);
		return list;
	}

	@Override
	public List<OrderResponse> findAllByTransactionType(String transactionType) {
		Query query = new Query(Criteria.where("transactionType").is(transactionType));
		List<OrderResponse> list=mongoTemplate.find(query, OrderResponse.class);
		return list;
	}

	@Override
	public List<OrderResponse> findAllByInstrumentToken(String instrumentToken) {
		Query query = new Query(Criteria.where("instrumentToken").is(instrumentToken));
		List<OrderResponse> list=mongoTemplate.find(query, OrderResponse.class);
		return list;
	}
}
