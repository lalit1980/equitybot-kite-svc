package com.equitybot.historicaldata.bar;

import org.ta4j.core.Decimal;

import java.util.Date;

public class BarModel {

    protected int barSize;
    protected Decimal highPrice;
    protected Decimal lowPrice;
    protected Decimal volume;
    protected Decimal openPrice;
    protected Decimal closePrice;
    protected Date timestamp;
    protected int tickCount;
    private long instrument;

    public BarModel() {
    }

    public BarModel(long instrument, int barSize) {
        this.instrument = instrument;
        this.barSize = barSize;
    }

    public long getInstrument() {
        return instrument;
    }

    public void setInstrument(long instrument) {
        this.instrument = instrument;
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        this.barSize = barSize;
    }

    public Decimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Decimal highPrice) {
        this.highPrice = highPrice;
    }

    public Decimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Decimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Decimal getVolume() {
        return volume;
    }

    public void setVolume(Decimal volume) {
        this.volume = volume;
    }

    public Decimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Decimal openPrice) {
        this.openPrice = openPrice;
    }

    public Decimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Decimal closePrice) {
        this.closePrice = closePrice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public String toString() {
        return "BarModel{" +
                "instrument=" + instrument +
                ", barSize=" + barSize +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", volume=" + volume +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", timestamp=" + timestamp +
                ", tickCount=" + tickCount +
                '}';
    }
}
