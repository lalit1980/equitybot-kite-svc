package com.equitybot.trade.db.mongodb.order.dto;

import java.io.Serializable;

public class HoldingsDTO implements Serializable {
	public String userId;
	public String product;
    public String lastPrice;
    public String price;
    public String tradingSymbol;
    public String t1Quantity;
    public String collateralQuantity;
    public String collateraltype;
    public String accountId;
    public String isin;
    public String pnl;
    public String quantity;
    public String realisedQuantity;
    public String averagePrice;
    public String exchange;
    public String instrumentToken;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTradingSymbol() {
		return tradingSymbol;
	}
	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}
	public String getT1Quantity() {
		return t1Quantity;
	}
	public void setT1Quantity(String t1Quantity) {
		this.t1Quantity = t1Quantity;
	}
	public String getCollateralQuantity() {
		return collateralQuantity;
	}
	public void setCollateralQuantity(String collateralQuantity) {
		this.collateralQuantity = collateralQuantity;
	}
	public String getCollateraltype() {
		return collateraltype;
	}
	public void setCollateraltype(String collateraltype) {
		this.collateraltype = collateraltype;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getPnl() {
		return pnl;
	}
	public void setPnl(String pnl) {
		this.pnl = pnl;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getRealisedQuantity() {
		return realisedQuantity;
	}
	public void setRealisedQuantity(String realisedQuantity) {
		this.realisedQuantity = realisedQuantity;
	}
	public String getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
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
	@Override
	public String toString() {
		return "HoldingsDTO [userId=" + userId + ", product=" + product + ", lastPrice=" + lastPrice + ", price="
				+ price + ", tradingSymbol=" + tradingSymbol + ", t1Quantity=" + t1Quantity + ", collateralQuantity="
				+ collateralQuantity + ", collateraltype=" + collateraltype + ", accountId=" + accountId + ", isin="
				+ isin + ", pnl=" + pnl + ", quantity=" + quantity + ", realisedQuantity=" + realisedQuantity
				+ ", averagePrice=" + averagePrice + ", exchange=" + exchange + ", instrumentToken=" + instrumentToken
				+ "]";
	}
}
