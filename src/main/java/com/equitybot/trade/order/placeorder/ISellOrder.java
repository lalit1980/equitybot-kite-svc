package com.equitybot.trade.order.placeorder;

import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.dto.OrderResponseDTO;

public interface ISellOrder extends IPlaceOrderService{
	public OrderResponseDTO sellOrder(OrderRequestDTO tradeOrderRequestDTO);
	
}
