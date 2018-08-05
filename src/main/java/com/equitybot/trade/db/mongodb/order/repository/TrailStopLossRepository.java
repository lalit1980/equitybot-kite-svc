package com.equitybot.trade.db.mongodb.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.order.domain.TrailStopLossResponse;

public interface TrailStopLossRepository extends MongoRepository<TrailStopLossResponse, String>, OrderResponseRespositoryCustom {

}
