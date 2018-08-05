package com.equitybot.trade.db.mongodb.order.repository;

import java.util.List;

import com.equitybot.trade.db.mongodb.order.domain.TrailStopLossResponse;

public interface TrailStopLossRespositoryCustom {

	public TrailStopLossResponse findByOrderId(String orderId);
	public List<TrailStopLossResponse> findAllByUserId(String userId);
	public List<TrailStopLossResponse> findAllByTradingSymbol(String tradingSymbol);
	public List<TrailStopLossResponse> findAllByOrderStatus(String orderStatus);
	public List<TrailStopLossResponse> findAllByTransactionType(String transactionType);
	public List<TrailStopLossResponse> findAllByInstrumentToken(String instrumentToken);
}
