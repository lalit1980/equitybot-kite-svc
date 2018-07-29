package com.equitybot.trade.ws.controller.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;

@RestController
@RequestMapping("/api")
public class InitiateKiteController {

	@Autowired
	private KiteConnectService tradePortZerodhaConnect;
	@Autowired
	IgniteConfig igniteConfig;
	
	private IgniteCache<Long, Double> cacheMaxTrailStopLoss;
	private IgniteCache<Long, Boolean> cacheTrailStopLossSignal;
	public InitiateKiteController() {
		CacheConfiguration<Long, Double> ccfgcacheMaxTrailStopLoss = new CacheConfiguration<Long, Double>("CacheMaxTrailStopLoss");
		this.cacheMaxTrailStopLoss = igniteConfig.getInstance().getOrCreateCache(ccfgcacheMaxTrailStopLoss);
		
		CacheConfiguration<Long, Boolean> ccfgcacheTrailStopLossSignal = new CacheConfiguration<Long, Boolean>("CacheTrailStopLossSignal");
		this.cacheTrailStopLossSignal = igniteConfig.getInstance().getOrCreateCache(ccfgcacheTrailStopLossSignal);
	}
	
	

	@GetMapping("/process/v1.0/{userId}/{requestToken}")
	public void getKiteConnectSession(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken) {
		try {
			tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken);
		} catch (JSONException | IOException | KiteException e) {
		}
	}

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{fromDate}/{toDate}/{instrumentToken}/{interval}/{continuous}")
	public ArrayList<List<Tick>> getHistoricalData(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken,
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@RequestBody ArrayList<Long> instrumentTokens, @PathVariable("interval") String interval) {
		try {
			KiteConnect kiteconnect = tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken);
			if(instrumentTokens!=null && instrumentTokens.size()>0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					this.cacheMaxTrailStopLoss.put(long1, 0.0);
					this.cacheTrailStopLossSignal.put(long1, false);
				}
			}
			return tradePortZerodhaConnect.startBackTesting(kiteconnect, instrumentTokens, fromDate, toDate, interval, false);
		} catch (JSONException | IOException | KiteException e) {
			return null;
		}
		
	}

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{backTestingFlag}")
	public void startTick(@PathVariable("userId") String userId, @PathVariable("requestToken") String requestToken,

			@RequestBody ArrayList<Long> instrumentTokens) {

		try {
			if(instrumentTokens!=null && instrumentTokens.size()>0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					this.cacheMaxTrailStopLoss.put(long1, 0.0);
					this.cacheTrailStopLossSignal.put(long1, false);
				}
			}
			
			tradePortZerodhaConnect.tickerUsage(tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken),
					instrumentTokens);
		} catch (IOException | WebSocketException | KiteException e) {
			e.printStackTrace();
		}

	}

}
