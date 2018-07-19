package com.equitybot.trade.db.mongodb.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;

public interface OrderResponseRepository extends MongoRepository<OrderResponse, String>, OrderResponseRespositoryCustom {

}
