package com.equitybot.trade.db.mongodb.instrument.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;

public interface InstrumentRepository extends MongoRepository<InstrumentModel, Long>, InstrumentRespositoryCustom {
	
}
