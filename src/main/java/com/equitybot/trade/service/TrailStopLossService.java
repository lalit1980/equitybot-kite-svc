package com.equitybot.trade.service;

import com.equitybot.trade.service.cache.CacheService;
import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TrailStopLossService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CacheService cacheService;

    public boolean breakDown(final Long instrument) {
        if (cacheService.bought(instrument)) {
            Tick latestTick = cacheService.latestTick(instrument);
            Tick maxTrailStopLossTick = cacheService.maxTrailStopLossTick(instrument);
            if (latestTick != null || maxTrailStopLossTick != null) {
                double breakDownMargin = maxTrailStopLossTick.getClosePrice() * 5 / 100;
                if (breakDownMargin < maxTrailStopLossTick.getClosePrice() - latestTick.getClosePrice()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                logger.error(" latestTick {} : or maxTrailStopLossTick {} is null for  instrument {}", latestTick,
                        maxTrailStopLossTick, instrument);
                throw new RuntimeException("latestTick " + latestTick + " :&: or maxTrailStopLossTick "
                        + maxTrailStopLossTick + " :&: is null for  instrument " + instrument);
            }
        } else {
            logger.debug(" instrument {} did not have position", instrument);
            return false;
        }
    }
}
