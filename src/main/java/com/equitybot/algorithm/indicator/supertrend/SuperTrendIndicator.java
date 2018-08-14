package com.equitybot.algorithm.indicator.supertrend;

import com.equitybot.algorithm.cache.AlgorithmCache;
import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.SuperTrendDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.ta4j.core.Decimal;

import java.util.concurrent.CompletableFuture;

import static com.equitybot.algorithm.indicator.supertrend.BaseIndicator.*;

@Service
public class SuperTrendIndicator {

    private static final Logger logger = LoggerFactory.getLogger(SuperTrendIndicator.class);


    @Autowired
    private AlgorithmCache algorithmCache;

    public static Decimal calculate(BarDTO workingBar, SuperTrendModel superTrendModel) {
        updateValues(workingBar, superTrendModel);
        if (superTrendModel.isInit) {
            calculate(superTrendModel);
            logger.info(" SuperTrendIndicator calculated for instrument {} ", superTrendModel.instrument);
        } else {
            logger.info(" SuperTrendIndicator :  instrument {} is in init stage", superTrendModel.instrument);
            init(superTrendModel);
        }
        return null;
    }

    private static Decimal calculate(SuperTrendModel superTrendModel) {
        superTrendModel.trueRange = calculateTrueRange(superTrendModel.previousBar, superTrendModel.bar);
        superTrendModel.initTrueRangeList.remove(0);
        superTrendModel.initTrueRangeList.add(superTrendModel.trueRange);
        superTrendModel.averageTrueRange = calculateEMA(superTrendModel.previousATR, superTrendModel.trueRange,
                smoothingConstant(superTrendModel.period));
        superTrendModel.basicUpperBand = calculateBasicUpperBand(superTrendModel.bar,
                superTrendModel.averageTrueRange, superTrendModel.multiplier);
        superTrendModel.basicLowerBand = calculateBasicLowerBand(superTrendModel.bar,
                superTrendModel.averageTrueRange, superTrendModel.multiplier);
        superTrendModel.finalUpperBand = calculateFinalUpperBand(superTrendModel.basicUpperBand,
                superTrendModel.previousFUB, superTrendModel.previousBar);
        superTrendModel.finalLowerBand = calculateFinalLowerBand(superTrendModel.basicLowerBand,
                superTrendModel.previousFLB, superTrendModel.previousBar);
        superTrendModel.superTrend = calculateSuperTrend(superTrendModel.bar, superTrendModel.finalUpperBand, superTrendModel.finalLowerBand, superTrendModel.previousFLB,
                superTrendModel.previousST, superTrendModel.previousFUB);
        superTrendModel.buySell = calculateBuySell(superTrendModel.bar, superTrendModel.superTrend);
        return superTrendModel.superTrend;
    }

    private static Decimal init(SuperTrendModel superTrendModel) {
        superTrendModel.trueRange = Decimal.ZERO;
        superTrendModel.averageTrueRange = Decimal.ZERO;
        superTrendModel.basicUpperBand = Decimal.ZERO;
        superTrendModel.basicLowerBand = Decimal.ZERO;
        superTrendModel.finalUpperBand = Decimal.ZERO;
        superTrendModel.finalLowerBand = Decimal.ZERO;
        superTrendModel.superTrend = Decimal.ZERO;

        if (superTrendModel.previousBar != null) {
            superTrendModel.trueRange = calculateTrueRange(superTrendModel.previousBar, superTrendModel.bar);
            superTrendModel.initTrueRangeList.add(superTrendModel.trueRange);
        }
        if (superTrendModel.initTrueRangeList.size() == superTrendModel.period) {
            superTrendModel.averageTrueRange = calculateSMA(superTrendModel.initTrueRangeList);
            superTrendModel.basicUpperBand = calculateBasicUpperBand(superTrendModel.bar,
                    superTrendModel.averageTrueRange, superTrendModel.multiplier);
            superTrendModel.basicLowerBand = calculateBasicLowerBand(superTrendModel.bar,
                    superTrendModel.averageTrueRange, superTrendModel.multiplier);
            superTrendModel.finalUpperBand = superTrendModel.basicUpperBand;
            superTrendModel.finalLowerBand = superTrendModel.basicLowerBand;
            superTrendModel.superTrend = calculateSuperTrend(superTrendModel.bar, superTrendModel.finalUpperBand,
                    superTrendModel.finalLowerBand, superTrendModel.previousFLB, superTrendModel.previousST, superTrendModel.previousFUB);
            superTrendModel.buySell = calculateBuySell(superTrendModel.bar, superTrendModel.superTrend);
            superTrendModel.isInit = true;
        }
        return superTrendModel.superTrend;
    }

    private static void updateValues(BarDTO workingBar, SuperTrendModel superTrendModel) {
        superTrendModel.previousFLB = superTrendModel.finalLowerBand;
        superTrendModel.previousFUB = superTrendModel.finalUpperBand;
        superTrendModel.previousATR = superTrendModel.averageTrueRange;
        superTrendModel.previousBar = superTrendModel.bar;
        superTrendModel.previousST = superTrendModel.getSuperTrend();
        superTrendModel.bar = workingBar;
    }

    private static SuperTrendDTO getSuperTrendDTO(SuperTrendModel superTrendModel) {
        return new SuperTrendDTO(superTrendModel.instrument, superTrendModel.period, superTrendModel.multiplier,
                superTrendModel.barSize, superTrendModel.trueRange, superTrendModel.averageTrueRange,
                superTrendModel.basicUpperBand, superTrendModel.basicLowerBand, superTrendModel.finalUpperBand,
                superTrendModel.finalLowerBand, superTrendModel.superTrend, superTrendModel.buySell);
    }

    private static Decimal smoothingConstant(int period) {
        return Decimal.ONE.dividedBy(period);
    }

    @Async("superTrendTaskPool")
    public CompletableFuture<SuperTrendDTO> generate(int multiplier, int period, BarDTO bar) {
        String name = Thread.currentThread().getName();
        System.out.println(" -- instrument : " + bar.getInstrument() + " # Thread Name : " + name +
                "  # barSize : " + bar.getBarSize() + " # multiplier : " + multiplier + " # period : " + period);

        String instrumentBarSizeMultiplierPeriod = bar.getInstrument() + "-" + bar.getBarSize()
                + "-" + multiplier + "-" + period;
        SuperTrendModel superTrendModel = this.algorithmCache.getSuperTrendModel(instrumentBarSizeMultiplierPeriod);
        if (superTrendModel == null) {
            superTrendModel = getNewSuperTrendModel(bar.getInstrument(), bar.getBarSize(), multiplier, period);
        }
        calculate(bar, superTrendModel);
        algorithmCache.putOnSuperTrendModelCache(instrumentBarSizeMultiplierPeriod, superTrendModel);
        SuperTrendDTO superTrendDTO = null;
        if (superTrendModel.buySell != null) {
            superTrendDTO = getSuperTrendDTO(superTrendModel);
            bar.getSuperTrends().add(superTrendDTO);
        }
        return CompletableFuture.completedFuture(superTrendDTO);
    }

    private SuperTrendModel getNewSuperTrendModel(long instrument, int barSize, int multiplier, int period) {
        return new SuperTrendModel(instrument, barSize, multiplier, period);
    }
}
