package com.equitybot.trade.db.mongodb.instrument.repository;

import java.util.List;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.mongodb.client.result.DeleteResult;

public interface InstrumentRespositoryCustom {

	public List<InstrumentModel> findByInstrumentToken(String instrumentToken);

	public List<InstrumentModel> findByExchangeToken(String exchangeToken);

	public List<InstrumentModel> findByTradingSymbol(String tradingSymbol);

	public List<InstrumentModel> findByName(String name);

	public List<InstrumentModel> findByNameLike(String nameRegX);

	public List<InstrumentModel> findByInstrumentType(String instrumentType);

	public List<Long>findBySegment(String segment);

	public List<InstrumentModel> findByExchange(String exchange);

	public List<Long> findByOptions(String exchange, String segment,
			String tradingSymbol);

	public List<InstrumentModel> findByEquity(String exchange, String instrumentType, String name, String segment,
			String tradingSymbol);

	public DeleteResult deleteInstrument(String instrumentToken);

	public void addAllInstruments(List<InstrumentModel> instrumentModel);
	public void deleteAllInstruments();
	
	
//	public List<InstrumentModel> findAllInstrumentFromKite(KiteConnect kiteConnect)  throws KiteException, IOException ;
	
}
