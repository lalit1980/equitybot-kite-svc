package com.equitybot.trade.db.mongodb.order.dto;

import java.io.Serializable;
import java.util.Date;

public class TradeResponseDTO implements Serializable{
	private String userId;
	public String tradeId;
	public String orderId;
	public String exchangeOrderId;
	public String tradingSymbol;
	public String exchange;
	public String instrumentToken;
	public String product;
	public String averagePrice;
	public String quantity;
	public Date fillTimestamp;
	public Date exchangeTimestamp;
	public String transactionType;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getExchangeOrderId() {
		return exchangeOrderId;
	}
	public void setExchangeOrderId(String exchangeOrderId) {
		this.exchangeOrderId = exchangeOrderId;
	}
	public String getTradingSymbol() {
		return tradingSymbol;
	}
	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(String instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Date getFillTimestamp() {
		return fillTimestamp;
	}
	public void setFillTimestamp(Date fillTimestamp) {
		this.fillTimestamp = fillTimestamp;
	}
	public Date getExchangeTimestamp() {
		return exchangeTimestamp;
	}
	public void setExchangeTimestamp(Date exchangeTimestamp) {
		this.exchangeTimestamp = exchangeTimestamp;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	@Override
	public String toString() {
		return "TradeResponseDTO [userId=" + userId + ", tradeId=" + tradeId + ", orderId=" + orderId
				+ ", exchangeOrderId=" + exchangeOrderId + ", tradingSymbol=" + tradingSymbol + ", exchange=" + exchange
				+ ", instrumentToken=" + instrumentToken + ", product=" + product + ", averagePrice=" + averagePrice
				+ ", quantity=" + quantity + ", fillTimestamp=" + fillTimestamp + ", exchangeTimestamp="
				+ exchangeTimestamp + ", transactionType=" + transactionType + "]";
	}
}
