package com.equitybot.trade.order.placeorder;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;

public interface IBuyOrder extends IPlaceOrderService{
	public OrderResponse buyOrder(OrderRequestDTO tradeOrderRequestDTO);
	public OrderResponse modifyOrder(OrderRequestDTO tradeOrderRequestDTO);
	public OrderResponse cancelOrder(OrderRequestDTO tradeOrderRequestDTO);
}
