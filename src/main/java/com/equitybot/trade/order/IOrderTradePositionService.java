package com.equitybot.trade.order;

import java.util.List;
import java.util.Map;

import com.equitybot.trade.db.mongodb.order.domain.HoldingsDTO;
import com.equitybot.trade.db.mongodb.order.domain.Position;
import com.equitybot.trade.db.mongodb.order.domain.TradeResponseDTO;
import com.equitybot.trade.db.mongodb.order.domain.UserProfileDTO;
import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;

public interface IOrderTradePositionService {
	public List<TradeResponseDTO> getOrders(KiteProperty kiteProperty);
	public List<TradeResponseDTO> getOrderHistory(String orderId);
	 /**
     * Retrieves list of trades executed.
     */
	public List<TradeResponseDTO> getTrades(KiteProperty kiteProperty);
	/**
     * Retrieves list of trades executed of an order.
     */
	public List<TradeResponseDTO> getOrderTrades(String orderId);
	 /**
     * Retrieves the list of holdings.
     */
	public List<HoldingsDTO> getHoldings(KiteProperty kiteProperty);
	
	/**
     * Retrieves the list of positions
     */
	 public Map<String, List<Position>> getPositions(KiteProperty kiteProperty);
	 
	 public UserProfileDTO getProfile(KiteProperty kiteProperty);
}
