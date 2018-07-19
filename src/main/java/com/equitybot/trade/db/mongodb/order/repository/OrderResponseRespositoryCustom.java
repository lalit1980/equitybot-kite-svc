package com.equitybot.trade.db.mongodb.order.repository;

import java.util.List;

import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;

public interface OrderResponseRespositoryCustom {

	public OrderResponse findByOrderId(String orderId);
	public List<OrderResponse> findAllByUserId(String userId);
	public List<OrderResponse> findAllByTradingSymbol(String tradingSymbol);
	public List<OrderResponse> findAllByOrderStatus(String orderStatus);
	public List<OrderResponse> findAllByTransactionType(String transactionType);
	public List<OrderResponse> findAllByInstrumentToken(String instrumentToken);
}
