package com.equitybot.trade.order.placeorder.converter;

import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.util.DateFormatUtil;
import com.zerodhatech.models.Order;

public class TradeDataConverter {

	public static OrderResponse convertToOrderDTO(Order order,long instrumentToken) {
		if (order != null) {
			OrderResponse dto = new OrderResponse();
			dto.setAccountId(order.accountId);
			dto.setAveragePrice(order.averagePrice);
			dto.setDisclosedQuantity(order.disclosedQuantity);
			dto.setExchange(order.exchange);
			dto.setExchangeOrderId(order.exchangeOrderId);
			dto.setExchangeTimestamp(DateFormatUtil.getCurrentISTTime(order.exchangeTimestamp));
			dto.setFilledQuantity(order.filledQuantity);
			dto.setInstrumentToken(instrumentToken);
			dto.setOrderId(order.orderId);
			dto.setOrderTimestamp(DateFormatUtil.getCurrentISTTime(order.orderTimestamp));
			dto.setOrderType(order.orderType);
			dto.setOrderVariety(order.orderVariety);
			dto.setParentOrderId(order.parentOrderId);
			dto.setPendingQuantity(order.pendingQuantity);
			dto.setPrice(order.price);
			dto.setProduct(order.product);
			dto.setQuantity(order.quantity);
			dto.setStatus(order.status);
			dto.setStatusMessage(order.statusMessage);
			dto.setSymbol(order.symbol);
			dto.setTag(order.tag);
			dto.setTradingSymbol(order.tradingSymbol);
			dto.setTransactionType(order.transactionType);
			dto.setTriggerPrice(order.triggerPrice);
			dto.setUserId(order.userId);
			dto.setValidity(order.validity);
			return dto;
		}
		return null;
	}
}
