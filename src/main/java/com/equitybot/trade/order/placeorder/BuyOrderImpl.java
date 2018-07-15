package com.equitybot.trade.order.placeorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.dto.OrderResponseDTO;
import com.equitybot.trade.order.placeorder.converter.TradeDataConverter;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.zerodhatech.models.Order;
@Service
public class BuyOrderImpl implements IBuyOrder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	KiteConnectService tradePortConnect;

	@Override
	public OrderResponseDTO buyOrder(OrderRequestDTO tradeOrderRequestDTO) {
		Order order = tradePortConnect.placeOrder(tradeOrderRequestDTO);
		OrderResponseDTO response=TradeDataConverter.convertToOrderDTO(order, tradeOrderRequestDTO.getInstrumentToken());
		response.setOrderId(order.orderId);
		logger.info("Placed Buy Order Response: "+response.toString());
		return response;
	}

	@Override
	public OrderResponseDTO modifyOrder(OrderRequestDTO tradeOrderRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponseDTO cancelOrder(OrderRequestDTO tradeOrderRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
