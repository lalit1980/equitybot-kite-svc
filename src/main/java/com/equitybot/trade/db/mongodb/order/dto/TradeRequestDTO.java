package com.equitybot.trade.db.mongodb.order.dto;

import java.io.Serializable;

public class TradeRequestDTO implements Serializable{

	private String userId;
	private String tradeId;
	private String parentOrderId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getParentOrderId() {
		return parentOrderId;
	}
	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	
}
