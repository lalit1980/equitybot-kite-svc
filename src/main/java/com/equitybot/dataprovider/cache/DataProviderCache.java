package com.equitybot.dataprovider.cache;

import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.bar.BarModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataProviderCache {

    private Map<String, BarModel> tickDataModelCache;

    private Map<Long, TickDTO> latestTickCache;

    public DataProviderCache() {
        tickDataModelCache = new HashMap<>();
        latestTickCache = new HashMap<>();
    }


    public BarModel getTickDataModel(String instrumentAndBarSize) {
        return this.tickDataModelCache.get(instrumentAndBarSize);
    }

    public void putOnTickDataModelCache(String instrumentAndBarSize, BarModel barModel) {
        this.tickDataModelCache.put(instrumentAndBarSize, barModel);
    }

    public TickDTO getLatestTick(Long instrument) {
        return this.latestTickCache.get(instrument);
    }

    public void putOnLatestTickCache(Long instrument, TickDTO tick) {
        this.latestTickCache.put(instrument, tick);
    }
}
