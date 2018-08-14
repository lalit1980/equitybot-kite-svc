package com.equitybot.algorithm.dao.supertrend.profit.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "superTrendProfitEntity")
public class SuperTrendProfitEntity {

    @Id
    private String id;
    @Indexed(name = "instrumentToken_index")
    private long instrumentToken;
    private String tradingSymbol;
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

    public double getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public void setTotalProfitLoss(double totalProfitLoss) {
        this.totalProfitLoss = totalProfitLoss;
    }

    @Override
    public String toString() {
        return "SuperTrendProfitEntity{" +
                "id='" + id + '\'' +
                ", instrumentToken=" + instrumentToken +
                ", tradingSymbol='" + tradingSymbol + '\'' +
                ", totalProfitLoss=" + totalProfitLoss +
                '}';
    }
}
