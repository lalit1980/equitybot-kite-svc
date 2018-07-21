package com.equitybot.trade.service.cache;

import com.equitybot.trade.service.cache.ignite.IgniteCache;
import com.zerodhatech.models.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private IgniteCache igniteCache;

    public boolean bought(Long instrument){
        return true;
    }

    public Tick latestTick(Long instrument){
        return igniteCache.getLatestTickCache().get(instrument);
    }

    public Tick maxTrailStopLossTick(Long instrument){
        return igniteCache.getMaxTrailStopLossTickCache().get(instrument);
    }
}
