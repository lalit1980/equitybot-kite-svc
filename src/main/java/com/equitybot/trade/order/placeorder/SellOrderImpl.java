package com.equitybot.trade.order.placeorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
@Service
public class SellOrderImpl implements ISellOrder{
	@Autowired
	KiteConnectService tradePortConnect;
	@Override
	public void sellOrder(OrderRequestDTO tradeOrderRequestDTO) {
		if(tradePortConnect.isBackTestFlag()) {
			tradePortConnect.placeMockOrder(tradeOrderRequestDTO);
		}else {
			tradePortConnect.placeOrder(tradeOrderRequestDTO);
		}
	}
}
