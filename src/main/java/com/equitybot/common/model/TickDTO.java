package com.equitybot.common.model;


import java.util.*;

public class TickDTO {
    private String mode;
    private boolean tradable;
    private long instrumentToken;
    private double lastTradedPrice;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double closePrice;
    private double change;
    private double lastTradedQuantity;
    private double averageTradePrice;
    private double volumeTradedToday;
    private double totalBuyQuantity;
    private double totalSellQuantity;
    private Date lastTradedTime;
    private double oi;
    private double oiDayHigh;
    private double oiDayLow;
    private Date tickTimestamp;
    private Map<String, ArrayList<DepthModel>> depth;
    private List<BarDTO> barDTOS;

    public TickDTO() {
        this.barDTOS = Collections.synchronizedList(new ArrayList<>());
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isTradable() {
        return tradable;
    }

    public void setTradable(boolean tradable) {
        this.tradable = tradable;
    }

    public long getInstrumentToken() {
        return instrumentToken;
    }

    public void setInstrumentToken(long instrumentToken) {
        this.instrumentToken = instrumentToken;
    }

    public double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getLastTradedQuantity() {
        return lastTradedQuantity;
    }

    public void setLastTradedQuantity(double lastTradedQuantity) {
        this.lastTradedQuantity = lastTradedQuantity;
    }

    public double getAverageTradePrice() {
        return averageTradePrice;
    }

    public void setAverageTradePrice(double averageTradePrice) {
        this.averageTradePrice = averageTradePrice;
    }

    public double getVolumeTradedToday() {
        return volumeTradedToday;
    }

    public void setVolumeTradedToday(double volumeTradedToday) {
        this.volumeTradedToday = volumeTradedToday;
    }

    public double getTotalBuyQuantity() {
        return totalBuyQuantity;
    }

    public void setTotalBuyQuantity(double totalBuyQuantity) {
        this.totalBuyQuantity = totalBuyQuantity;
    }

    public double getTotalSellQuantity() {
        return totalSellQuantity;
    }

    public void setTotalSellQuantity(double totalSellQuantity) {
        this.totalSellQuantity = totalSellQuantity;
    }

    public Date getLastTradedTime() {
        return lastTradedTime;
    }

    public void setLastTradedTime(Date lastTradedTime) {
        this.lastTradedTime = lastTradedTime;
    }

    public double getOi() {
        return oi;
    }

    public void setOi(double oi) {
        this.oi = oi;
    }

    public double getOiDayHigh() {
        return oiDayHigh;
    }

    public void setOiDayHigh(double oiDayHigh) {
        this.oiDayHigh = oiDayHigh;
    }

    public double getOiDayLow() {
        return oiDayLow;
    }

    public void setOiDayLow(double oiDayLow) {
        this.oiDayLow = oiDayLow;
    }

    public Date getTickTimestamp() {
        return tickTimestamp;
    }

    public void setTickTimestamp(Date tickTimestamp) {
        this.tickTimestamp = tickTimestamp;
    }

    public Map<String, ArrayList<DepthModel>> getDepth() {
        return depth;
    }

    public void setDepth(Map<String, ArrayList<DepthModel>> depth) {
        this.depth = depth;
    }

    public List<BarDTO> getBarDTOS() {
        return barDTOS;
    }

    public void setBarDTOS(List<BarDTO> barDTOS) {
        this.barDTOS = barDTOS;
    }

    @Override
    public String toString() {
        return "TickDTO{" +
                "mode='" + mode + '\'' +
                ", tradable=" + tradable +
                ", instrumentToken=" + instrumentToken +
                ", lastTradedPrice=" + lastTradedPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", change=" + change +
                ", lastTradedQuantity=" + lastTradedQuantity +
                ", averageTradePrice=" + averageTradePrice +
                ", volumeTradedToday=" + volumeTradedToday +
                ", totalBuyQuantity=" + totalBuyQuantity +
                ", totalSellQuantity=" + totalSellQuantity +
                ", lastTradedTime=" + lastTradedTime +
                ", oi=" + oi +
                ", oiDayHigh=" + oiDayHigh +
                ", oiDayLow=" + oiDayLow +
                ", tickTimestamp=" + tickTimestamp +
                ", depth=" + depth +
                ", barDTOS=" + barDTOS +
                '}';
    }
}
