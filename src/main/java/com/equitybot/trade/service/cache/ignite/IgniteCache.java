package com.equitybot.trade.service.cache.ignite;

import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.zerodhatech.models.Tick;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IgniteCache {

    @Autowired
    IgniteConfig igniteConfig;
    private org.apache.ignite.IgniteCache<Long, Tick> maxTrailStopLossTickCache;
    private org.apache.ignite.IgniteCache<Long, Tick> latestTickCache;

    public IgniteCache(){
        CacheConfiguration<Long, Tick> ccfgLatestTickParams = new CacheConfiguration<>("CachedLatestTick");
        ccfgLatestTickParams.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        ccfgLatestTickParams.setCacheMode(CacheMode.PARTITIONED);
        ccfgLatestTickParams.setRebalanceMode(CacheRebalanceMode.NONE);
        ccfgLatestTickParams.setDataRegionName("1GB_Region");
        this.latestTickCache = igniteConfig.getInstance().getOrCreateCache(ccfgLatestTickParams);

        CacheConfiguration<Long, Tick> ccfgMaxTrailStopLossTickCacheParams = new CacheConfiguration<>("MaxTrailStopLossTickCache");
        ccfgMaxTrailStopLossTickCacheParams.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        ccfgMaxTrailStopLossTickCacheParams.setCacheMode(CacheMode.PARTITIONED);
        ccfgMaxTrailStopLossTickCacheParams.setRebalanceMode(CacheRebalanceMode.NONE);
        ccfgMaxTrailStopLossTickCacheParams.setDataRegionName("1GB_Region");
        this.maxTrailStopLossTickCache = igniteConfig.getInstance().getOrCreateCache(ccfgMaxTrailStopLossTickCacheParams);
    }

    public org.apache.ignite.IgniteCache<Long, Tick> getMaxTrailStopLossTickCache() {
        return maxTrailStopLossTickCache;
    }

    public org.apache.ignite.IgniteCache<Long, Tick> getLatestTickCache() {
        return latestTickCache;
    }
}
