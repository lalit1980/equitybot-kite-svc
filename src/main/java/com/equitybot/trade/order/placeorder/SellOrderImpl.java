package com.equitybot.trade.order.placeorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.order.placeorder.converter.TradeDataConverter;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.zerodhatech.models.Order;
@Service
public class SellOrderImpl implements ISellOrder{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	KiteConnectService tradePortConnect;
	@Override
	public OrderResponse sellOrder(OrderRequestDTO tradeOrderRequestDTO) {
		Order order = tradePortConnect.placeOrder(tradeOrderRequestDTO);
		OrderResponse response=TradeDataConverter.convertToOrderDTO(order, tradeOrderRequestDTO.getInstrumentToken());
		response.setOrderId(order.orderId);
		logger.info("Placed Sell Order Response: "+response.toString());
		return response;
	}

	


}
