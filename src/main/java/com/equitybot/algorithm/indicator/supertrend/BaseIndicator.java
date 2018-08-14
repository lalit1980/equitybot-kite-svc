package com.equitybot.algorithm.indicator.supertrend;

import com.equitybot.algorithm.constants.Constant;
import com.equitybot.common.model.BarDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.ta4j.core.Decimal;

import java.util.List;

@Service
public class BaseIndicator {

    private static final Logger logger = LoggerFactory.getLogger(BaseIndicator.class);

    private BaseIndicator() {
    }

    public static Decimal calculateTrueRange(BarDTO previousBar, BarDTO workingBar) {
        logger.info(" * added new True Range for instrument");
        return workingBar.getHighPrice().minus(workingBar.getLowPrice())
                .max((workingBar.getHighPrice().minus(previousBar.getClosePrice()).abs()))
                .max((workingBar.getLowPrice().minus(previousBar.getClosePrice()).abs()));
    }

    public static Decimal calculateSMA(List<Decimal> trueRangeList) {
        logger.info(" * calculate SMA");
        Decimal sma = Decimal.valueOf(0);
        for (Decimal trueRange : trueRangeList) {
            sma = sma.plus(trueRange);
        }
        sma = sma.dividedBy(trueRangeList.size());
        return sma;

    }

    public static Decimal calculateEMA(Decimal previousEMA, Decimal workingTR, Decimal smoothingConstant) {
        logger.info(" * calculate EMA");
        return smoothingConstant.multipliedBy(workingTR.minus(previousEMA)).plus(previousEMA);
    }

    public static Decimal calculateBasicUpperBand(BarDTO workingBar, Decimal workingEMA, int multiplier) {
        logger.info(" * calculate Basic Upper Band");
        return workingBar.getHighPrice().plus(workingBar.getLowPrice()).dividedBy(Decimal.TWO)
                .plus(workingEMA.multipliedBy(multiplier));
    }

    public static Decimal calculateBasicLowerBand(BarDTO workingBar, Decimal workingEMA, int multiplier) {
        logger.info(" * calculate Basic Lower Band");
        return workingBar.getHighPrice().plus(workingBar.getLowPrice()).dividedBy(Decimal.TWO)
                .minus(workingEMA.multipliedBy(multiplier));
    }

    public static Decimal calculateFinalUpperBand(Decimal workingBUB, Decimal previousFUB, BarDTO previousBar) {
        logger.info(" * calculate Final Upper Band");
        Decimal finalUpperBand;
        if (workingBUB.isLessThan(previousFUB) || previousBar.getClosePrice().isGreaterThan(previousFUB)) {
            finalUpperBand = workingBUB;
        } else {
            finalUpperBand = previousFUB;
        }
        return finalUpperBand;
    }

    public static Decimal calculateFinalLowerBand(Decimal workingBLB, Decimal previousFLB, BarDTO previousBar) {
        logger.info(" * calculate Final Lower Band");
        Decimal finalLowerBand;
        if (workingBLB.isGreaterThan(previousFLB) || previousBar.getClosePrice().isLessThan(previousFLB)) {
            finalLowerBand = workingBLB;
        } else {
            finalLowerBand = previousFLB;
        }
        return finalLowerBand;
    }

    public static Decimal calculateSuperTrend(BarDTO workingBar, Decimal workingFUB, Decimal workingFLB, Decimal previousFLB, Decimal previousST, Decimal previousFUB) {
        logger.info(" * adding new Super Trend");
        Decimal superTrend;
        if (previousST.equals(previousFUB) && workingBar.getClosePrice().isLessThanOrEqual(workingFUB)) {
            superTrend = workingFUB;
        } else if (previousST.equals(previousFUB) && workingBar.getClosePrice().isGreaterThan(workingFUB)) {
            superTrend = workingFLB;

        } else if (previousST.equals(previousFLB) && workingBar.getClosePrice().isGreaterThanOrEqual(workingFLB)) {
            superTrend = workingFLB;

        } else if (previousST.equals(previousFLB) && workingBar.getClosePrice().isLessThan(workingFLB)) {
            superTrend = workingFUB;
        } else {
            superTrend = Decimal.ZERO;
        }
        return superTrend;
    }

    public static String calculateBuySell(BarDTO workingBar, Decimal workingSuperTrend) {
        logger.info(" * Adding new Super Trend BuyS Sell");
        String superTrendBuySell;
        if (workingBar.getClosePrice().isLessThan(workingSuperTrend)) {
            superTrendBuySell = Constant.SELL;
        } else {
            superTrendBuySell = Constant.BUY;
        }
        return superTrendBuySell;
    }

}