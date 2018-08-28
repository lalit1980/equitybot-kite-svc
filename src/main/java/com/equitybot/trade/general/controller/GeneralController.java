package com.equitybot.trade.general.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.zerodhatech.kiteconnect.utils.Constants;

@RestController
@RequestMapping("/api")
public class GeneralController {
	
	@Autowired
	IgniteConfig igniteConfig;
	
	@Autowired
	private KiteConnectService tradePortZerodhaConnect;
	
	private IgniteCache<Long, Double> cacheStopLossValue;
	private IgniteCache<Long, Integer> cacheQuantity;
	private IgniteCache<Long, String> cacheTradeOrder;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public GeneralController() {
		CacheConfiguration<Long, Double> ccfgcStopLoss = new CacheConfiguration<Long, Double>("CacheStopLoss");
		this.cacheStopLossValue = igniteConfig.getInstance().getOrCreateCache(ccfgcStopLoss);
		
		CacheConfiguration<Long, Integer> ccfgcQuantity = new CacheConfiguration<Long, Integer>("CacheQuantity");
		this.cacheQuantity = igniteConfig.getInstance().getOrCreateCache(ccfgcQuantity);
		
		CacheConfiguration<Long, String> ccfgOrderDetails = new CacheConfiguration<Long, String>("CachedTradeOrder");
		this.cacheTradeOrder = igniteConfig.getInstance().getOrCreateCache(ccfgOrderDetails);
		
	}

	@PostMapping({ "/general/v1.0/{instrumentToken}/{quantity}" })
	public String addQuantity(@PathVariable("instrumentToken") Long instrumentToken,
			@PathVariable("quantity") int quantity) {
		cacheQuantity.put(instrumentToken, quantity);
		return "Instrument Quantity: "+cacheQuantity.get(instrumentToken);
	}
	
	@PostMapping({ "/general/v1.0/stopLoss/{instrumentToken}/{stopLoss}" })
	public String addStopLoss(@PathVariable("instrumentToken") Long instrumentToken,
			@PathVariable("stopLoss") double stopLoss) {
		cacheStopLossValue.put(instrumentToken, stopLoss);
		return "Instrument Stop Loss: "+cacheStopLossValue.get(instrumentToken);
	}

	@PutMapping("/general/v1.0/{instrumentToken}/{quantity}/{stopLoss}")
	public void update(@PathVariable("instrumentToken") Long instrumentToken,
			@PathVariable("quantity") int quantity,
			@PathVariable("stopLoss") double stopLoss) {
		cacheQuantity.put(instrumentToken, quantity);
		cacheStopLossValue.put(instrumentToken, stopLoss);
	}

	@DeleteMapping({ "/general/v1.0/{instrumentToken}" })
	public void delete(@PathVariable("instrumentToken") Long instrumentToken) {
		if(cacheQuantity.containsKey(instrumentToken)) {
			cacheQuantity.remove(instrumentToken);
		}
		if(cacheStopLossValue.containsKey(instrumentToken)) {
			cacheStopLossValue.remove(instrumentToken);
		}
	}
	@GetMapping("/general/v1.0")
	public Map<Long, String> findAllOrderInCache() {
		Set<Long> keys = new HashSet<Long>();
		this.cacheTradeOrder.query(new ScanQuery<>(null)).forEach(entry -> keys.add((Long) entry.getKey()));
		Map<Long, String> map=this.cacheTradeOrder.getAll(keys);
		for (Map.Entry<Long, String> entry : map.entrySet()) {
			logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		return map;
	}
	
	@DeleteMapping({ "/general/v1.0/deleteOrderCache/{instrumentToken}" })
	public void deleteOrderFromCache(@PathVariable("instrumentToken") Long instrumentToken) {
		if(this.cacheTradeOrder.containsKey(instrumentToken)) {
			this.cacheTradeOrder.remove(instrumentToken);
		}
		if(this.cacheTradeOrder.containsKey(instrumentToken)) {
			this.cacheTradeOrder.remove(instrumentToken);
		}
	}
	@PostMapping({ "/general/v1.0/addOrderInCache/{instrumentToken}/{signal}" })
	public String addTradeOrdeInCache(@PathVariable("instrumentToken") Long instrumentToken,
			@PathVariable("signal") String signal) {
		this.cacheTradeOrder.put(instrumentToken, signal.toUpperCase().trim());
		return "Instrument Stop Loss: "+this.cacheTradeOrder.get(instrumentToken);
	}
	
	@DeleteMapping({ "/general/v1.0/deleteOrderCache" })
	public void deleteAllOrderFromCache() {
		this.cacheTradeOrder.removeAll();
	}
	@GetMapping("/general/v1.0/getOrderInCache/{instrumentToken}")
	public String findOrderInCache(@PathVariable("instrumentToken") Long tradingSymbol) {
		return this.cacheTradeOrder.get(tradingSymbol);
	}
	
	@PutMapping("/general/v1.0/updateTrailStolossFlag/{calculateStopLossFlag}")
	public void updateTrailStopLossFlag(@PathVariable("calculateStopLossFlag") Boolean calculateStopLossFlag) {
		tradePortZerodhaConnect.setCalculateStopLossFlag(calculateStopLossFlag);
	}
	
	@PutMapping("/general/v1.0/updateDayTradingAllowed/{dayTradingAllowed}")
	public void updateDayTradingAllowed(@PathVariable("dayTradingAllowed") Boolean dayTradingAllowed) {
		tradePortZerodhaConnect.setDayTradingAllowed(dayTradingAllowed);
	}
	@PutMapping("/general/v1.0/updateDayTarget/{dayTradingAllowed}")
	public void updateDayTarget(@PathVariable("dayTargetAmount") Double dayTargetAmount) {
		tradePortZerodhaConnect.setDayTarget(dayTargetAmount);
	}
	
}
