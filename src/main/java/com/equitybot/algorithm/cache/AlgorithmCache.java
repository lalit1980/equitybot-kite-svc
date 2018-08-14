package com.equitybot.algorithm.cache;

import com.equitybot.algorithm.indicator.supertrend.SuperTrendModel;
import com.equitybot.common.config.IgniteConfig;
import com.zerodhatech.models.Instrument;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AlgorithmCache {

    private IgniteCache<Long, Instrument> instrumentCache;
    private Map<String, SuperTrendModel> superTrendModelCache;

    public AlgorithmCache() {
        this.superTrendModelCache = new HashMap<>();
        CacheConfiguration<Long, Instrument> ccfgcacheInstrument = new CacheConfiguration<>("CacheInstrument");
        this.instrumentCache = IgniteConfig.getInstance().getOrCreateCache(ccfgcacheInstrument);
    }

    public Instrument getInstrument(Long instrument) {
        return this.instrumentCache.get(instrument);
    }

    public void putOnInstrumentCache(Long instrumentToken, Instrument instrument) {
        this.instrumentCache.put(instrumentToken, instrument);
    }

    public SuperTrendModel getSuperTrendModel(String instrumentBarSizeMultiplierPeriod) {
        return this.superTrendModelCache.get(instrumentBarSizeMultiplierPeriod);
    }

    public void putOnSuperTrendModelCache(String instrumentBarSizeMultiplierPeriod, SuperTrendModel superTrendModel) {
        this.superTrendModelCache.put(instrumentBarSizeMultiplierPeriod, superTrendModel);
    }

}
