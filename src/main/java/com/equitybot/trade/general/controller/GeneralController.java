package com.equitybot.trade.general.controller;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.ignite.configs.IgniteConfig;

@RestController
@RequestMapping("/api")
public class GeneralController {
	
	@Autowired
	IgniteConfig igniteConfig;
	private IgniteCache<String, Double> cacheStopLossValue;
	private IgniteCache<String, Integer> cacheQuantity;
	
	public GeneralController() {
		CacheConfiguration<String, Double> ccfgcStopLoss = new CacheConfiguration<String, Double>("CacheStopLoss");
		this.cacheStopLossValue = igniteConfig.getInstance().getOrCreateCache(ccfgcStopLoss);
		
		CacheConfiguration<String, Integer> ccfgcQuantity = new CacheConfiguration<String, Integer>("CacheQuantity");
		this.cacheQuantity = igniteConfig.getInstance().getOrCreateCache(ccfgcQuantity);
		
	}

	@PostMapping({ "/general/v1.0/{instrumentToken}/{quantity}" })
	public String addQuantity(@PathVariable("instrumentToken") String instrumentToken,
			@PathVariable("quantity") int quantity) {
		cacheQuantity.put(instrumentToken, quantity);
		return "Instrument Quantity: "+cacheQuantity.get(instrumentToken);
	}
	
	@PostMapping({ "/general/v1.0/stopLoss/{instrumentToken}/{stopLoss}" })
	public String addStopLoss(@PathVariable("instrumentToken") String instrumentToken,
			@PathVariable("stopLoss") double stopLoss) {
		cacheStopLossValue.put(instrumentToken, stopLoss);
		return "Instrument Stop Loss: "+cacheStopLossValue.get(instrumentToken);
	}

	@PutMapping("/general/v1.0/{instrumentToken}/{quantity}/{stopLoss}")
	public void update(@PathVariable("instrumentToken") String instrumentToken,
			@PathVariable("quantity") int quantity,
			@PathVariable("stopLoss") double stopLoss) {
		cacheQuantity.put(instrumentToken, quantity);
		cacheStopLossValue.put(instrumentToken, stopLoss);
		
	}

	@DeleteMapping({ "/general/v1.0/{instrumentToken}" })
	public void delete(@PathVariable("instrumentToken") String instrumentToken) {
		if(cacheQuantity.containsKey(instrumentToken)) {
			cacheQuantity.remove(instrumentToken);
		}
		if(cacheStopLossValue.containsKey(instrumentToken)) {
			cacheStopLossValue.remove(instrumentToken);
		}
	}
	
}
