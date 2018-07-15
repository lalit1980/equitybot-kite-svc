package com.equitybot.trade.order.placeorder;

import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.dto.OrderResponseDTO;

public interface IBuyOrder extends IPlaceOrderService{
	public OrderResponseDTO buyOrder(OrderRequestDTO tradeOrderRequestDTO);
	public OrderResponseDTO modifyOrder(OrderRequestDTO tradeOrderRequestDTO);
	public OrderResponseDTO cancelOrder(OrderRequestDTO tradeOrderRequestDTO);
}
