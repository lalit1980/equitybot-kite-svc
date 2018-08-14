package com.equitybot.algorithm.dao.supertrend.detailed.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "SuperTrendDetailedEntity")
public class SuperTrendDetailedEntity {

    @Id
    private String id;
    @Indexed(name = "instrumentToken_index")
    private long instrumentToken;
    private String tradingSymbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private double trueRange;
    private double ema;
    private double basicUpperBand;
    private double basicLowerBand;
    private double finalUpperBand;
    private double finalLowerBand;
    private double superTrend;
    private String signal;
    private Date transactionTime;
    private double profitAndLoss;
    private double totalProfitLoss;
    private String type;

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

    public double getTrueRange() {
        return trueRange;
    }

    public void setTrueRange(double trueRange) {
        this.trueRange = trueRange;
    }

    public double getEma() {
        return ema;
    }

    public void setEma(double ema) {
        this.ema = ema;
    }

    public double getBasicUpperBand() {
        return basicUpperBand;
    }

    public void setBasicUpperBand(double basicUpperBand) {
        this.basicUpperBand = basicUpperBand;
    }

    public double getBasicLowerBand() {
        return basicLowerBand;
    }

    public void setBasicLowerBand(double basicLowerBand) {
        this.basicLowerBand = basicLowerBand;
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

    public double getSuperTrend() {
        return superTrend;
    }

    public void setSuperTrend(double superTrend) {
        this.superTrend = superTrend;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SuperTrendDetailedEntity{" +
                "id='" + id + '\'' +
                ", instrumentToken=" + instrumentToken +
                ", tradingSymbol='" + tradingSymbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", trueRange=" + trueRange +
                ", ema=" + ema +
                ", basicUpperBand=" + basicUpperBand +
                ", basicLowerBand=" + basicLowerBand +
                ", finalUpperBand=" + finalUpperBand +
                ", finalLowerBand=" + finalLowerBand +
                ", superTrend=" + superTrend +
                ", signal='" + signal + '\'' +
                ", transactionTime=" + transactionTime +
                ", profitAndLoss=" + profitAndLoss +
                ", totalProfitLoss=" + totalProfitLoss +
                ", type='" + type + '\'' +
                '}';
    }
}
