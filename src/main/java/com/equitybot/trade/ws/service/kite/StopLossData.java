package com.equitybot.trade.ws.service.kite;

public class StopLossData {
	private double stopLoss;
	private double trailStopLoss;
	private double purchasePrice;
	private long instrumentToken;
	public double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public double getTrailStopLoss() {
		return trailStopLoss;
	}
	public void setTrailStopLoss(double trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public long getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	@Override
	public String toString() {
		return "StopLossData [stopLoss=" + stopLoss + ", trailStopLoss=" + trailStopLoss + ", purchasePrice="
				+ purchasePrice + ", instrumentToken=" + instrumentToken + "]";
	}
}
