package com.equitybot.trade.db.mongodb.property.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;

public interface PropertyRepository extends MongoRepository<KiteProperty, Long>, PropertyRespositoryCustom {

}
