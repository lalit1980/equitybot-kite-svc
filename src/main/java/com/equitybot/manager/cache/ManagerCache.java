package com.equitybot.manager.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zerodhatech.models.Position;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ManagerCache {
	
    private Map<Long, Double> lastTradedPriceCache;
	private Map<Long, String> tradeOrderCache;
	private Map<Long, Double> purchasedPriceCache;
	private Map<Long, Double> maxTrailStopLossCache;
	private Map<Long, Double> totalProfitCache;
	private Map<Long, Double> stopLossDistanceCache;
	private Map<Long, Integer> quantityCache;
	private Map<Long, String> instrumentTradingSymbolCache;
	private Map<Long, Boolean> startTradeCache;
	private Map<String, String> userSessionCache;
	private Map<String, Double> totalProfitAndLossCache;
	private Map<Long, String> productTypeCache;
	private Map<Long,Position> positionCache;
	

	
	public ManagerCache() {
		this.lastTradedPriceCache = new ConcurrentHashMap<>();
		this.tradeOrderCache = new ConcurrentHashMap<>();
		this.purchasedPriceCache = new ConcurrentHashMap<>();
		this.maxTrailStopLossCache = new ConcurrentHashMap<>();
		this.totalProfitCache = new ConcurrentHashMap<>();
		this.stopLossDistanceCache = new ConcurrentHashMap<>();
		this.quantityCache = new ConcurrentHashMap<>();
		this.instrumentTradingSymbolCache = new ConcurrentHashMap<>();
		this.startTradeCache = new ConcurrentHashMap<>();
		this.userSessionCache = new ConcurrentHashMap<>();
		this.totalProfitAndLossCache = new ConcurrentHashMap<>();
		this.productTypeCache = new ConcurrentHashMap<>();
		this.positionCache = new ConcurrentHashMap<>();
	}
	
	public Double getLastTradedPrice(Long instrument) {
        return this.lastTradedPriceCache.get(instrument);
    }

    public void putOnLastTradedPriceCache(Long instrument, Double tradedPrice) {
        this.lastTradedPriceCache.put( instrument, tradedPrice);
    }
    
    public String getTradeOrder(Long instrument) {
        return this.tradeOrderCache.get(instrument);
    }

    public void putOnTradeOrderCache(Long instrument, String tradeOrder) {
        this.tradeOrderCache.put( instrument, tradeOrder);
    }
    
    public Double getPurchasedPrice(Long instrument) {
        return this.purchasedPriceCache.get(instrument);
    }

    public void putOnPurchasedPriceCache(Long instrument, Double purchasedPrice) {
        this.purchasedPriceCache.put( instrument, purchasedPrice);
    }
    
    public Double getMaxTrailStopLoss(Long instrument) {
        return this.maxTrailStopLossCache.get(instrument);
    }

    public void putOnMaxTrailStopLossCache(Long instrument, Double maxTrailStopLoss) {
        this.maxTrailStopLossCache.put( instrument, maxTrailStopLoss);
    }
    
    public Double getTotalProfit(Long instrument) {
        return this.totalProfitCache.get(instrument);
    }

    public void putOnTotalProfitCache(Long instrument, Double totalProfit) {
        this.totalProfitCache.put( instrument, totalProfit);
    }
    
    public Double getStopLossDistance(Long instrument) {
        return this.stopLossDistanceCache.get(instrument);
    }

    public void putOnStopLossDistanceCache(Long instrument, Double stopLossDistance) {
        this.stopLossDistanceCache.put( instrument, stopLossDistance);
    }
    
    public Integer getQuantity(Long instrument) {
        return this.quantityCache.get(instrument);
    }

    public void putOnQuantityCache(Long instrument, Integer quantity) {
        this.quantityCache.put( instrument, quantity);
    }
    
    public String getInstrumentTradingSymbol(Long instrument) {
        return this.instrumentTradingSymbolCache.get(instrument);
    }

    public void putOnInstrumentTradingSymbolCache(Long instrument, String instrumentTradingSymbol) {
        this.instrumentTradingSymbolCache.put( instrument, instrumentTradingSymbol);
    }
    
    public Boolean getStartTrade(Long instrument) {
        return this.startTradeCache.get(instrument);
    }

    public void putOnStartTradeCache(Long instrument, Boolean startTrade) {
        this.startTradeCache.put( instrument, startTrade);
    }
    
    public String getUserSession(String user) {
        return this.userSessionCache.get(user);
    }

    public void putOnUserSessionCache(String user, String userSession) {
        this.userSessionCache.put( user, userSession);
    }
    
    public Double getTotalProfitAndLoss(String instrument) {
        return this.totalProfitAndLossCache.get(instrument);
    }

    public void putOnTotalProfitAndLossCache(String instrument, Double totalProfitAndLoss) {
        this.totalProfitAndLossCache.put( instrument, totalProfitAndLoss);
    }
    
    public String getProductType(Long instrument) {
        return this.productTypeCache.get(instrument);
    }

    public void putOnProductTypeCache(Long instrument, String productType) {
        this.productTypeCache.put( instrument, productType);
    }
    
    public Position getPosition(Long instrument) {
        return this.positionCache.get(instrument);
    }

    public void putOnPositionCache(Long instrument, Position position) {
        this.positionCache.put( instrument, position);
    }
    
    

}
