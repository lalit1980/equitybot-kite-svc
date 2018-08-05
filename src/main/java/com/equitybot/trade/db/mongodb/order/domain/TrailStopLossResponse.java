package com.equitybot.trade.db.mongodb.order.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "TrailStopLossResponse")
public class TrailStopLossResponse {
	@Id
	private String id;
	@Indexed(name = "it_trail_index")
	private long instrumentToken;
	private String tradingSymbol;
	private double buyPrice;
	private double sellPrice;
	private String type;
	private String buyTransactionTime;
	private String sellTransactionTime;
	private double profitAndLoss;
	private double totalProfitLoss;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	public String getTradingSymbol() {
		return tradingSymbol;
	}
	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBuyTransactionTime() {
		return buyTransactionTime;
	}
	public void setBuyTransactionTime(String buyTransactionTime) {
		this.buyTransactionTime = buyTransactionTime;
	}
	public String getSellTransactionTime() {
		return sellTransactionTime;
	}
	public void setSellTransactionTime(String sellTransactionTime) {
		this.sellTransactionTime = sellTransactionTime;
	}
	public double getProfitAndLoss() {
		return profitAndLoss;
	}
	public void setProfitAndLoss(double profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}
	public double getTotalProfitLoss() {
		return totalProfitLoss;
	}
	public void setTotalProfitLoss(double totalProfitLoss) {
		this.totalProfitLoss = totalProfitLoss;
	}
	@Override
	public String toString() {
		return "TrailStopLossResponse [id=" + id + ", instrumentToken=" + instrumentToken + ", tradingSymbol="
				+ tradingSymbol + ", buyPrice=" + buyPrice + ", sellPrice=" + sellPrice + ", type=" + type
				+ ", buyTransactionTime=" + buyTransactionTime + ", sellTransactionTime=" + sellTransactionTime
				+ ", profitAndLoss=" + profitAndLoss + ", totalProfitLoss=" + totalProfitLoss + "]";
	}

}
