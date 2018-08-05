package com.equitybot.trade.db.mongodb.order.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "OrderResponse")
public class OrderResponse{
	    @Indexed(name = "inst_order_res_index")
		private long instrumentToken;
		@Id
	    private String exchangeOrderId;
	    private String disclosedQuantity;
	    private String validity;
	    private String tradingSymbol;
	    private String orderVariety;
	    private String userId;
	    private String orderType;
	    private String triggerPrice;
	    private String statusMessage;
	    private String price;
	    private String status;
	    private String product;
	    private String accountId;
	    private String exchange;
	    private String orderId;
	    private String symbol;
	    private String pendingQuantity;
	    private String orderTimestamp;
	    private String exchangeTimestamp;
	    private String averagePrice;
	    private String transactionType;
	    private String filledQuantity;
	    private String quantity;
	    private String parentOrderId;
	    private String tag;
		public long getInstrumentToken() {
			return instrumentToken;
		}
		public void setInstrumentToken(long instrumentToken) {
			this.instrumentToken = instrumentToken;
		}
		public String getExchangeOrderId() {
			return exchangeOrderId;
		}
		public void setExchangeOrderId(String exchangeOrderId) {
			this.exchangeOrderId = exchangeOrderId;
		}
		public String getDisclosedQuantity() {
			return disclosedQuantity;
		}
		public void setDisclosedQuantity(String disclosedQuantity) {
			this.disclosedQuantity = disclosedQuantity;
		}
		public String getValidity() {
			return validity;
		}
		public void setValidity(String validity) {
			this.validity = validity;
		}
		public String getTradingSymbol() {
			return tradingSymbol;
		}
		public void setTradingSymbol(String tradingSymbol) {
			this.tradingSymbol = tradingSymbol;
		}
		public String getOrderVariety() {
			return orderVariety;
		}
		public void setOrderVariety(String orderVariety) {
			this.orderVariety = orderVariety;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getOrderType() {
			return orderType;
		}
		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}
		public String getTriggerPrice() {
			return triggerPrice;
		}
		public void setTriggerPrice(String triggerPrice) {
			this.triggerPrice = triggerPrice;
		}
		public String getStatusMessage() {
			return statusMessage;
		}
		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getProduct() {
			return product;
		}
		public void setProduct(String product) {
			this.product = product;
		}
		public String getAccountId() {
			return accountId;
		}
		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}
		public String getExchange() {
			return exchange;
		}
		public void setExchange(String exchange) {
			this.exchange = exchange;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getPendingQuantity() {
			return pendingQuantity;
		}
		public void setPendingQuantity(String pendingQuantity) {
			this.pendingQuantity = pendingQuantity;
		}
		public String getOrderTimestamp() {
			return orderTimestamp;
		}
		public void setOrderTimestamp(String orderTimestamp) {
			this.orderTimestamp = orderTimestamp;
		}
		public String getExchangeTimestamp() {
			return exchangeTimestamp;
		}
		public void setExchangeTimestamp(String exchangeTimestamp) {
			this.exchangeTimestamp = exchangeTimestamp;
		}
		public String getAveragePrice() {
			return averagePrice;
		}
		public void setAveragePrice(String averagePrice) {
			this.averagePrice = averagePrice;
		}
		public String getTransactionType() {
			return transactionType;
		}
		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}
		public String getFilledQuantity() {
			return filledQuantity;
		}
		public void setFilledQuantity(String filledQuantity) {
			this.filledQuantity = filledQuantity;
		}
		public String getQuantity() {
			return quantity;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		public String getParentOrderId() {
			return parentOrderId;
		}
		public void setParentOrderId(String parentOrderId) {
			this.parentOrderId = parentOrderId;
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		@Override
		public String toString() {
			return "TradeOrderResponseDTO [instrumentToken=" + instrumentToken + ", exchangeOrderId=" + exchangeOrderId
					+ ", disclosedQuantity=" + disclosedQuantity + ", validity=" + validity + ", tradingSymbol="
					+ tradingSymbol + ", orderVariety=" + orderVariety + ", userId=" + userId + ", orderType="
					+ orderType + ", triggerPrice=" + triggerPrice + ", statusMessage=" + statusMessage + ", price="
					+ price + ", status=" + status + ", product=" + product + ", accountId=" + accountId + ", exchange="
					+ exchange + ", orderId=" + orderId + ", symbol=" + symbol + ", pendingQuantity=" + pendingQuantity
					+ ", orderTimestamp=" + orderTimestamp + ", exchangeTimestamp=" + exchangeTimestamp
					+ ", averagePrice=" + averagePrice + ", transactionType=" + transactionType + ", filledQuantity="
					+ filledQuantity + ", quantity=" + quantity + ", parentOrderId=" + parentOrderId + ", tag=" + tag
					+ "]";
		}
}
