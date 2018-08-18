package com.equitybot.trade.ws.controller.main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.equitybot.trade.db.mongodb.property.repository.PropertyRepository;
import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Tick;

@RestController
@RequestMapping("/api")
public class InitiateKiteController {

	@Autowired
	private KiteConnectService tradePortZerodhaConnect;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	IgniteConfig igniteConfig;

	private IgniteCache<Long, Double> cacheMaxTrailStopLoss;
	private IgniteCache<Long, Boolean> cacheTrailStopLossSignal;
	private IgniteCache<Long, Instrument> cacheInstrument;
	private IgniteCache<Long, Double> stopLoss;
	private IgniteCache<Long, Integer> quantity;

	private IgniteCache<String, Double> cacheStopLossValue;
	private IgniteCache<String, Integer> cacheQuantity;

	private IgniteCache<Long, Boolean> startTrade;
	private IgniteCache<String, Double> cacheTargetPrice;
	private IgniteCache<String, KiteConnect> cacheUserSession;

	public InitiateKiteController() {
		CacheConfiguration<Long, Double> ccfgcacheMaxTrailStopLoss = new CacheConfiguration<Long, Double>(
				"CacheMaxTrailStopLoss");
		this.cacheMaxTrailStopLoss = igniteConfig.getInstance().getOrCreateCache(ccfgcacheMaxTrailStopLoss);

		CacheConfiguration<Long, Boolean> ccfgcacheTrailStopLossSignal = new CacheConfiguration<Long, Boolean>(
				"CacheTrailStopLossSignal");
		this.cacheTrailStopLossSignal = igniteConfig.getInstance().getOrCreateCache(ccfgcacheTrailStopLossSignal);

		CacheConfiguration<Long, Instrument> ccfgcacheInstrument = new CacheConfiguration<Long, Instrument>(
				"CacheInstrument");
		this.cacheInstrument = igniteConfig.getInstance().getOrCreateCache(ccfgcacheInstrument);

		CacheConfiguration<String, Double> ccfgcStopLoss = new CacheConfiguration<String, Double>("CacheStopLoss");
		this.cacheStopLossValue = igniteConfig.getInstance().getOrCreateCache(ccfgcStopLoss);

		CacheConfiguration<String, Integer> ccfgcQuantity = new CacheConfiguration<String, Integer>("CacheQuantity");
		this.cacheQuantity = igniteConfig.getInstance().getOrCreateCache(ccfgcQuantity);

		CacheConfiguration<Long, Boolean> ccfgcStartTrade = new CacheConfiguration<Long, Boolean>("CacheStartTrade");
		this.startTrade = igniteConfig.getInstance().getOrCreateCache(ccfgcStartTrade);

		CacheConfiguration<String, Double> ccfgcTargetPrice = new CacheConfiguration<String, Double>(
				"CacheTargetPrice");
		this.cacheTargetPrice = igniteConfig.getInstance().getOrCreateCache(ccfgcTargetPrice);

		CacheConfiguration<String, KiteConnect> ccfgcKiteSession = new CacheConfiguration<String, KiteConnect>(
				"CacheUserSession");
		this.cacheUserSession = igniteConfig.getInstance().getOrCreateCache(ccfgcKiteSession);
	}

	@GetMapping("/process/v1.0/{userId}/{requestToken}")
	public void getKiteConnectSession(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken) {
		try {
			tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken);
		} catch (JSONException | IOException | KiteException e) {
		}
	}

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{fromDate}/{toDate}/{instrumentToken}/{interval}/{continuous}")
	public ArrayList<List<Tick>> getHistoricalData(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken,
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@RequestBody ArrayList<Long> instrumentTokens, @PathVariable("interval") String interval)
			throws ParseException {
		tradePortZerodhaConnect.setBackTestFlag(true);
		try {
			KiteConnect kiteconnect = tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken);
			this.cacheUserSession.put(userId, kiteconnect);
			List<Instrument> list = tradePortZerodhaConnect.getAllInstruments(kiteconnect);
			if (list != null && list.size() > 0) {
				for (Instrument instrument : list) {
					this.cacheInstrument.put(instrument.getInstrument_token(), instrument);
					cacheStopLossValue.put(instrument.getTradingsymbol(), 20.00);
					cacheQuantity.put(instrument.getTradingsymbol(), 2);
				}
			}
			if (instrumentTokens != null && instrumentTokens.size() > 0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					this.cacheMaxTrailStopLoss.put(long1, 0.0);
					this.cacheTrailStopLossSignal.put(long1, false);
					startTrade.put(long1, true);
				}
			}
			tradePortZerodhaConnect.setBackTestFlag(true);
			return tradePortZerodhaConnect.startBackTesting(kiteconnect, instrumentTokens, fromDate,
					toDate, interval, false);
		} catch (JSONException | IOException | KiteException e) {
			return null;
		}

	}

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{dayMonth}")
	public void startTick(@PathVariable("userId") String userId, @PathVariable("requestToken") String requestToken,
			@RequestBody ArrayList<Long> instrumentTokens) throws ParseException {

		try {
			tradePortZerodhaConnect.setBackTestFlag(true);
			KiteConnect kiteconnect = tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken);
			this.cacheUserSession.put(userId, kiteconnect);
			List<Instrument> list = tradePortZerodhaConnect.getAllInstruments(kiteconnect);
			if (list != null && list.size() > 0) {
				for (Instrument instrument : list) {
					this.cacheInstrument.put(instrument.getInstrument_token(), instrument);
				}
			}

			if (instrumentTokens != null && instrumentTokens.size() > 0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					this.cacheMaxTrailStopLoss.put(long1, 0.0);
					this.cacheTrailStopLossSignal.put(long1, false);
					Instrument instrument = this.cacheInstrument.get(long1);
					cacheStopLossValue.put(instrument.getTradingsymbol(), 20.00);
					this.cacheTargetPrice.put(instrument.getTradingsymbol(), 5.00);
					this.cacheQuantity.put(instrument.getTradingsymbol(), 2);
					startTrade.put(long1, false);
				}
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long dayFriction = 86400000L;
			Date historicalToDate = dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis())));
			Date historicalFromDate = dateFormat
					.parse(dateFormat.format(new Date(historicalToDate.getTime() - (dayFriction))));
			tradePortZerodhaConnect.startBackTesting(kiteconnect, instrumentTokens, historicalFromDate,
					historicalToDate, "minute", false);
			if (instrumentTokens != null && instrumentTokens.size() > 0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					startTrade.put(long1, true);
				}
			}
			tradePortZerodhaConnect.setBackTestFlag(false);
			tradePortZerodhaConnect.tickerUsage(kiteconnect, instrumentTokens);
		} catch (IOException | WebSocketException | KiteException e) {
			e.printStackTrace();
		}

	}

}
