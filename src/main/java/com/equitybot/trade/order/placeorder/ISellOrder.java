package com.equitybot.trade.order.placeorder;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;

public interface ISellOrder extends IPlaceOrderService{
	public void sellOrder(OrderRequestDTO tradeOrderRequestDTO);
	
}
