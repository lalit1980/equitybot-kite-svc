package com.equitybot.trade.db.mongodb.tick.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.tick.domain.Tick;

public interface TickRepository extends MongoRepository<Tick, Long>, TickRespositoryCustom {

}
