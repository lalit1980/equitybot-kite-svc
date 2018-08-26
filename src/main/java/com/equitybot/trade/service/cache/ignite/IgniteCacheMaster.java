package com.equitybot.trade.service.cache.ignite;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.zerodhatech.models.Tick;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IgniteCacheMaster {

    @Autowired
    IgniteConfig igniteConfig;
    private IgniteCache<Long, Tick> maxTrailStopLossTickCache;
    private IgniteCache<Long, Double> cacheLastTradedPrice;

    public IgniteCacheMaster(){
    	CacheConfiguration<Long, Double> ccfg = new CacheConfiguration<Long, Double>("LastTradedPrice");
		this.cacheLastTradedPrice = igniteConfig.getInstance().getOrCreateCache(ccfg);

        CacheConfiguration<Long, Tick> ccfgMaxTrailStopLossTickCacheParams = new CacheConfiguration<>("MaxTrailStopLossTickCache");
        this.maxTrailStopLossTickCache = igniteConfig.getInstance().getOrCreateCache(ccfgMaxTrailStopLossTickCacheParams);
    }

    public org.apache.ignite.IgniteCache<Long, Tick> getMaxTrailStopLossTickCache() {
        return maxTrailStopLossTickCache;
    }

	public IgniteCache<Long, Double> getCacheLastTradedPrice() {
		return cacheLastTradedPrice;
	}

   
}
