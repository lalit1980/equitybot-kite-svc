package com.equitybot.common.model;

import org.ta4j.core.Decimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BarDTO {

    private long instrument;
    private int barSize;
    private Decimal highPrice;
    private Decimal lowPrice;
    private Decimal openPrice;
    private Decimal closePrice;
    private Decimal volume;
    private Date timestamp;
    private List<SuperTrendDTO> superTrends;

    public BarDTO() {
        this.superTrends = new ArrayList<>();
    }

    public BarDTO(long instrument, int barSize, Decimal highPrice, Decimal lowPrice, Decimal openPrice,
                  Decimal closePrice, Decimal volume, Date timestamp) {
        this.instrument = instrument;
        this.barSize = barSize;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.timestamp = timestamp;
        this.superTrends = new LinkedList<>();
    }

    public BarDTO(long instrument, int barSize, Decimal highPrice, Decimal lowPrice, Decimal openPrice,
                  Decimal closePrice, Decimal volume, Date timestamp, List<SuperTrendDTO> superTrends) {
        this.instrument = instrument;
        this.barSize = barSize;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.timestamp = timestamp;
        this.superTrends = superTrends;
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

    public Decimal getVolume() {
        return volume;
    }

    public void setVolume(Decimal volume) {
        this.volume = volume;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<SuperTrendDTO> getSuperTrends() {
        return superTrends;
    }

    public void setSuperTrends(List<SuperTrendDTO> superTrends) {
        this.superTrends = superTrends;
    }

    @Override
    public String toString() {
        return "BarDTO{" +
                "instrument=" + instrument +
                ", barSize=" + barSize +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                ", timestamp=" + timestamp +
                ", superTrends=" + superTrends +
                '}';
    }
}
