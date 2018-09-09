package com.equitybot.kite.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zerodhatech.models.Instrument;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KiteCache {
	
    private Map<Long, Instrument> instrumentCache;
    
    public KiteCache() {
        this.instrumentCache = new ConcurrentHashMap<>();
    }
    
    public Instrument getinstrumentObj(Long instrument) {
        return this.instrumentCache.get(instrument);
    }

    public void putOnInstrumentCache(Long instrument, Instrument instrumentObj) {
        this.instrumentCache.put( instrument, instrumentObj);
    }

}
