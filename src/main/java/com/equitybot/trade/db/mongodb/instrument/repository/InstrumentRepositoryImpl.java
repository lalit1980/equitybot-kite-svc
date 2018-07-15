package com.equitybot.trade.db.mongodb.instrument.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.DeleteResult;

public class InstrumentRepositoryImpl implements InstrumentRespositoryCustom {

	@Autowired
	MongoOperations mongoTemplate;

	public InstrumentModel findByInstrumentToken(String instrumentToken) {
		List<InstrumentModel> list=mongoTemplate.find(new Query(Criteria.where("instrumentToken").is(Long.parseLong(instrumentToken))),
				InstrumentModel.class);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<InstrumentModel> findByExchangeToken(String exchangeToken) {
		return mongoTemplate.find(new Query(Criteria.where("exchange_token").is(Long.parseLong(exchangeToken))), InstrumentModel.class);
	}

	@Override
	public List<InstrumentModel> findByTradingSymbol(String tradingSymbol) {
		
		BasicQuery query = 
				new BasicQuery("{\"tradingSymbol\": {$regex : '" + tradingSymbol + "'} }");
			query.limit(10);
		return mongoTemplate.find(query, InstrumentModel.class);

	}

	@Override
	public List<InstrumentModel> findByName(String name) {
		return mongoTemplate.find(new Query(Criteria.where("name").is(name)), InstrumentModel.class);
	}

	@Override
	public List<InstrumentModel> findByNameLike(String nameRegX) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").regex(nameRegX, "i"));
		return mongoTemplate.find(query, InstrumentModel.class);
	}

	@Override
	public List<InstrumentModel> findByInstrumentType(String instrumentType) {
		return mongoTemplate.find(new Query(Criteria.where("instrument_type").is(instrumentType)),
				InstrumentModel.class);
	}

	@Override
	public List<Long> findBySegment(String segment) {
		List<InstrumentModel> list=mongoTemplate.find(new Query(Criteria.where("segment").is(segment)), InstrumentModel.class);
		List<Long> instrumentTokens=new ArrayList<Long>();
		if(list!=null && list.size()>0) {
			list.forEach(instrumentModel -> {
				instrumentTokens.add(instrumentModel.getInstrumentToken());
			});
		}
		
		return instrumentTokens;
	}

	@Override
	public List<InstrumentModel> findByExchange(String exchange) {
		return mongoTemplate.find(new Query(Criteria.where("exchange").is(exchange)), InstrumentModel.class);
	}

	@Override
	public List<Long> findByOptions(String exchange, String segment, String tradingSymbol) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("tradingSymbol").regex(tradingSymbol).and("segment").is(segment).and("exchange").is(exchange));
		
		List<InstrumentModel> list=mongoTemplate.find(query, InstrumentModel.class);
		
		List<Long> instrumentTokens=new ArrayList<Long>();
		if(list!=null && list.size()>0) {
			list.forEach(instrumentModel -> {
				instrumentTokens.add(instrumentModel.getInstrumentToken());
			});
		}
		return instrumentTokens;

		//return list;
	}

	@Override
	public List<InstrumentModel> findByEquity(String exchange, String instrumentType, String nameRegX, String segment,
			String tradingSymbol) {
		return mongoTemplate.find(new Query(Criteria.where("exchange").is(exchange).and("instrument_type")
				.is(instrumentType).and("name").regex(nameRegX, "i").and("tradingSymbol").is(tradingSymbol)),
				InstrumentModel.class);
	}

	@Override
	public void addAllInstruments(List<InstrumentModel> instruments) {
		BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED, InstrumentModel.class);
		instruments.forEach(instrument -> {
			bulkOperations.insert(instrument);
		});
		BulkWriteResult result = bulkOperations.execute();
		
	}

	@Override
	public DeleteResult deleteInstrument(String instrumentToken) {
		return mongoTemplate.remove(instrumentToken);
	}

	@Override
	public void deleteAllInstruments() {
		mongoTemplate.remove(new Query(), "Instrument");

	}

	
}
