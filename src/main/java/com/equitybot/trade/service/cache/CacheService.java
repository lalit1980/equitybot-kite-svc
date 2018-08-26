package com.equitybot.trade.service.cache;

import com.equitybot.trade.service.cache.ignite.IgniteCacheMaster;
import com.zerodhatech.models.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private IgniteCacheMaster igniteCache;

    public boolean bought(Long instrument){
        return true;
    }

    public Double latestTick(Long instrument){
        return igniteCache.getCacheLastTradedPrice().get(instrument);
    }

    public Tick maxTrailStopLossTick(Long instrument){
        return igniteCache.getMaxTrailStopLossTickCache().get(instrument);
    }
}
