package com.equitybot.trade.bo;

public class NormalTradeOrderRequestBO {

	private long instrumentToken;
	private String userId;
	private String requestToken;
	private long quantity;
	private String orderType;
	private String tradingsymbol;
	private String product;
	private String exchange;
	private String transactionType;
	private String validity;
	private double price;
	private double triggerPrice;
	private String tag;
	public long getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTradingsymbol() {
		return tradingsymbol;
	}
	public void setTradingsymbol(String tradingsymbol) {
		this.tradingsymbol = tradingsymbol;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTriggerPrice() {
		return triggerPrice;
	}
	public void setTriggerPrice(double triggerPrice) {
		this.triggerPrice = triggerPrice;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public String toString() {
		return "NormalTradeOrderRequestBO [instrumentToken=" + instrumentToken + ", userId=" + userId
				+ ", requestToken=" + requestToken + ", quantity=" + quantity + ", orderType=" + orderType
				+ ", tradingsymbol=" + tradingsymbol + ", product=" + product + ", exchange=" + exchange
				+ ", transactionType=" + transactionType + ", validity=" + validity + ", price=" + price
				+ ", triggerPrice=" + triggerPrice + ", tag=" + tag + "]";
	}
	
}
