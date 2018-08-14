package com.equitybot.algorithm.indicator.supertrend;

import com.equitybot.common.model.BarDTO;
import org.ta4j.core.Decimal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SuperTrendModel {

    protected long instrument;
    protected int period;
    protected int multiplier;
    protected int barSize;
    protected boolean isInit;
    protected List<Decimal> initTrueRangeList;
    protected BarDTO previousBar;
    protected Decimal previousFUB;
    protected Decimal previousFLB;
    protected Decimal previousATR;
    protected Decimal previousST;


    protected Decimal trueRange;
    protected Decimal averageTrueRange;
    protected Decimal basicUpperBand;
    protected Decimal basicLowerBand;
    protected Decimal finalUpperBand;
    protected Decimal finalLowerBand;
    protected Decimal superTrend;
    protected String buySell;
    protected BarDTO bar;

    public SuperTrendModel() {
    }

    public SuperTrendModel(long instrument, int barSize, int multiplier, int period) {
        this.period = period;
        this.multiplier = multiplier;
        this.instrument = instrument;
        this.barSize = barSize;
        this.isInit = false;
        this.initTrueRangeList = Collections.synchronizedList(new LinkedList<>());
    }

    public long getInstrument() {
        return instrument;
    }

    public int getPeriod() {
        return period;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getBarSize() {
        return barSize;
    }

    public Decimal getTrueRange() {
        return trueRange;
    }

    public Decimal getAverageTrueRange() {
        return averageTrueRange;
    }

    public Decimal getBasicUpperBand() {
        return basicUpperBand;
    }

    public Decimal getBasicLowerBand() {
        return basicLowerBand;
    }

    public Decimal getFinalUpperBand() {
        return finalUpperBand;
    }

    public Decimal getFinalLowerBand() {
        return finalLowerBand;
    }

    public Decimal getSuperTrend() {
        return superTrend;
    }

    public String getBuySell() {
        return buySell;
    }

    @Override
    public String toString() {
        return "SuperTrendModel{" +
                "instrument=" + instrument +
                ", period=" + period +
                ", multiplier=" + multiplier +
                ", barSize=" + barSize +
                ", isInit=" + isInit +
                ", initTrueRangeList=" + initTrueRangeList +
                ", previousBar=" + previousBar +
                ", previousFUB=" + previousFUB +
                ", previousFLB=" + previousFLB +
                ", previousATR=" + previousATR +
                ", previousST=" + previousST +
                ", trueRange=" + trueRange +
                ", averageTrueRange=" + averageTrueRange +
                ", basicUpperBand=" + basicUpperBand +
                ", basicLowerBand=" + basicLowerBand +
                ", finalUpperBand=" + finalUpperBand +
                ", finalLowerBand=" + finalLowerBand +
                ", superTrend=" + superTrend +
                ", buySell='" + buySell + '\'' +
                ", bar=" + bar +
                '}';
    }
}
