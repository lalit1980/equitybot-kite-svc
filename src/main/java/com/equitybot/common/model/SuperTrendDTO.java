package com.equitybot.common.model;

import org.ta4j.core.Decimal;

public class SuperTrendDTO {

    private Decimal trueRange;
    private Decimal averageTrueRange;
    private Decimal basicUpperBand;
    private Decimal basicLowerBand;
    private Decimal finalUpperBand;
    private Decimal finalLowerBand;
    private Decimal superTrend;
    private String buySell;
    private long instrument;
    private int period;
    private int multiplier;
    private int barSize;

    public SuperTrendDTO() {
    }

    public SuperTrendDTO(long instrument, int period, int multiplier, int barSize, Decimal trueRange,
                         Decimal averageTrueRange, Decimal basicUpperBand, Decimal basicLowerBand,
                         Decimal finalUpperBand, Decimal finalLowerBand, Decimal superTrend, String buySell) {
        this.instrument = instrument;
        this.period = period;
        this.multiplier = multiplier;
        this.barSize = barSize;
        this.trueRange = trueRange;
        this.averageTrueRange = averageTrueRange;
        this.basicUpperBand = basicUpperBand;
        this.basicLowerBand = basicLowerBand;
        this.finalUpperBand = finalUpperBand;
        this.finalLowerBand = finalLowerBand;
        this.superTrend = superTrend;
        this.buySell = buySell;
    }

    public long getInstrument() {
        return instrument;
    }

    public void setInstrument(long instrument) {
        this.instrument = instrument;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        this.barSize = barSize;
    }

    public Decimal getTrueRange() {
        return trueRange;
    }

    public void setTrueRange(Decimal trueRange) {
        this.trueRange = trueRange;
    }

    public Decimal getAverageTrueRange() {
        return averageTrueRange;
    }

    public void setAverageTrueRange(Decimal averageTrueRange) {
        this.averageTrueRange = averageTrueRange;
    }

    public Decimal getBasicUpperBand() {
        return basicUpperBand;
    }

    public void setBasicUpperBand(Decimal basicUpperBand) {
        this.basicUpperBand = basicUpperBand;
    }

    public Decimal getBasicLowerBand() {
        return basicLowerBand;
    }

    public void setBasicLowerBand(Decimal basicLowerBand) {
        this.basicLowerBand = basicLowerBand;
    }

    public Decimal getFinalUpperBand() {
        return finalUpperBand;
    }

    public void setFinalUpperBand(Decimal finalUpperBand) {
        this.finalUpperBand = finalUpperBand;
    }

    public Decimal getFinalLowerBand() {
        return finalLowerBand;
    }

    public void setFinalLowerBand(Decimal finalLowerBand) {
        this.finalLowerBand = finalLowerBand;
    }

    public Decimal getSuperTrend() {
        return superTrend;
    }

    public void setSuperTrend(Decimal superTrend) {
        this.superTrend = superTrend;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    @Override
    public String toString() {
        return "SuperTrendDTO{" +
                "instrument=" + instrument +
                ", period=" + period +
                ", multiplier=" + multiplier +
                ", barSize=" + barSize +
                ", trueRange=" + trueRange +
                ", averageTrueRange=" + averageTrueRange +
                ", basicUpperBand=" + basicUpperBand +
                ", basicLowerBand=" + basicLowerBand +
                ", finalUpperBand=" + finalUpperBand +
                ", finalLowerBand=" + finalLowerBand +
                ", superTrend=" + superTrend +
                ", buySell='" + buySell + '\'' +
                '}';
    }
}
