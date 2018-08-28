package com.equitybot.algorithm.cache;

import com.equitybot.algorithm.indicator.supertrend.SuperTrendModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AlgorithmCache {

    private Map<String, SuperTrendModel> superTrendModelCache;

    public AlgorithmCache() {
        this.superTrendModelCache = new ConcurrentHashMap<>();
    }

    public SuperTrendModel getSuperTrendModel(String instrumentBarSizeMultiplierPeriod) {
        return this.superTrendModelCache.get(instrumentBarSizeMultiplierPeriod);
    }

    public void putOnSuperTrendModelCache(String instrumentBarSizeMultiplierPeriod, SuperTrendModel superTrendModel) {
        this.superTrendModelCache.put(instrumentBarSizeMultiplierPeriod, superTrendModel);
    }

}