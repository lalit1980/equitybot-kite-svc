package com.equitybot.trade.db.mongodb.timeseries.domain;

import java.util.Date;

public class Bar {
	private long instrumentToken;
	private double open;
	private double high;
	private double low;
	private double close;
	private Date barBeginTime;
	private Date barEndTime;
	private double trueRange;
	private long volume;
	private double basicUpperBand;
	private double bsicLowerBand;
	private double finalUpperBand;
	private double finalLowerBand;
	private String signal;
	public long getInstrumentToken() {
		return instrumentToken;
	}
	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public Date getBarBeginTime() {
		return barBeginTime;
	}
	public void setBarBeginTime(Date barBeginTime) {
		this.barBeginTime = barBeginTime;
	}
	public Date getBarEndTime() {
		return barEndTime;
	}
	public void setBarEndTime(Date barEndTime) {
		this.barEndTime = barEndTime;
	}
	public double getTrueRange() {
		return trueRange;
	}
	public void setTrueRange(double trueRange) {
		this.trueRange = trueRange;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public double getBasicUpperBand() {
		return basicUpperBand;
	}
	public void setBasicUpperBand(double basicUpperBand) {
		this.basicUpperBand = basicUpperBand;
	}
	public double getBsicLowerBand() {
		return bsicLowerBand;
	}
	public void setBsicLowerBand(double bsicLowerBand) {
		this.bsicLowerBand = bsicLowerBand;
	}
	public double getFinalUpperBand() {
		return finalUpperBand;
	}
	public void setFinalUpperBand(double finalUpperBand) {
		this.finalUpperBand = finalUpperBand;
	}
	public double getFinalLowerBand() {
		return finalLowerBand;
	}
	public void setFinalLowerBand(double finalLowerBand) {
		this.finalLowerBand = finalLowerBand;
	}
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	@Override
	public String toString() {
		return "Bar [instrumentToken=" + instrumentToken + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", barBeginTime=" + barBeginTime + ", barEndTime=" + barEndTime + ", trueRange="
				+ trueRange + ", volume=" + volume + ", basicUpperBand=" + basicUpperBand + ", bsicLowerBand="
				+ bsicLowerBand + ", finalUpperBand=" + finalUpperBand + ", finalLowerBand=" + finalLowerBand
				+ ", signal=" + signal + "]";
	}

}
