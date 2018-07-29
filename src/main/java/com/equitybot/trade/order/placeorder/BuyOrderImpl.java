package com.equitybot.trade.order.placeorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
@Service
public class BuyOrderImpl implements IBuyOrder {
	@Autowired
	KiteConnectService tradePortConnect;

	@Override
	public void buyOrder(OrderRequestDTO tradeOrderRequestDTO) {
		tradePortConnect.placeOrder(tradeOrderRequestDTO);
	}

	@Override
	public OrderResponse modifyOrder(OrderRequestDTO tradeOrderRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse cancelOrder(OrderRequestDTO tradeOrderRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
