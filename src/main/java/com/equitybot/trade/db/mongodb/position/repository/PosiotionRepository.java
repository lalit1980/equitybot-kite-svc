package com.equitybot.trade.db.mongodb.position.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.db.mongodb.order.domain.Position;

public interface PosiotionRepository extends MongoRepository<Position, String>, PositionCustom {

}
