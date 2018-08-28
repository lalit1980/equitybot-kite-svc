package com.equitybot.trade.ws.controller.main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.instrument.repository.InstrumentRepository;
import com.equitybot.trade.db.mongodb.property.repository.PropertyRepository;
import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.equitybot.trade.util.DateFormatUtil;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Tick;

@RestController
@RequestMapping("/api")
public class InitiateKiteController {

	@Autowired
	private KiteConnectService tradePortZerodhaConnect;

	@Autowired
	private PropertyRepository propertyRepository;
	
	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	IgniteConfig igniteConfig;

	private IgniteCache<Long, Double> cacheMaxTrailStopLoss;
	private IgniteCache<Long, Boolean> cacheTrailStopLossSignal;
	private IgniteCache<Long, String> cacheInstrumentTradingSymbol;
	private IgniteCache<Long, Double> cacheStopLossValue;
	private IgniteCache<Long, Integer> cacheQuantity;

	private IgniteCache<Long, Boolean> startTrade;
	private IgniteCache<Long, Double> cacheTargetPrice;
	private IgniteCache<String, KiteConnect> cacheUserSession;
	private IgniteCache<Long, String> cacheTradeOrder;

	public InitiateKiteController() {
		CacheConfiguration<Long, Double> ccfgcacheMaxTrailStopLoss = new CacheConfiguration<Long, Double>(
				"CacheMaxTrailStopLoss");
		this.cacheMaxTrailStopLoss = igniteConfig.getInstance().getOrCreateCache(ccfgcacheMaxTrailStopLoss);

		CacheConfiguration<Long, Boolean> ccfgcacheTrailStopLossSignal = new CacheConfiguration<Long, Boolean>(
				"CacheTrailStopLossSignal");
		this.cacheTrailStopLossSignal = igniteConfig.getInstance().getOrCreateCache(ccfgcacheTrailStopLossSignal);

		CacheConfiguration<Long, String> ccfgcacheInstrument = new CacheConfiguration<Long, String>(
				"CacheInstrumentTradingSymbol");
		this.cacheInstrumentTradingSymbol = igniteConfig.getInstance().getOrCreateCache(ccfgcacheInstrument);

		CacheConfiguration<Long, Double> ccfgcStopLoss = new CacheConfiguration<Long, Double>("CacheStopLoss");
		this.cacheStopLossValue = igniteConfig.getInstance().getOrCreateCache(ccfgcStopLoss);

		CacheConfiguration<Long, Integer> ccfgcQuantity = new CacheConfiguration<Long, Integer>("CacheQuantity");
		this.cacheQuantity = igniteConfig.getInstance().getOrCreateCache(ccfgcQuantity);

		CacheConfiguration<Long, Boolean> ccfgcStartTrade = new CacheConfiguration<Long, Boolean>("CacheStartTrade");
		this.startTrade = igniteConfig.getInstance().getOrCreateCache(ccfgcStartTrade);

		CacheConfiguration<Long, Double> ccfgcTargetPrice = new CacheConfiguration<Long, Double>(
				"CacheTargetPrice");
		this.cacheTargetPrice = igniteConfig.getInstance().getOrCreateCache(ccfgcTargetPrice);

		CacheConfiguration<String, KiteConnect> ccfgcKiteSession = new CacheConfiguration<String, KiteConnect>(
				"CacheUserSession");
		this.cacheUserSession = igniteConfig.getInstance().getOrCreateCache(ccfgcKiteSession);
		
		CacheConfiguration<Long, String> ccfgOrderDetails = new CacheConfiguration<Long, String>("CachedTradeOrder");
		this.cacheTradeOrder = igniteConfig.getInstance().getOrCreateCache(ccfgOrderDetails);
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
			
			List<InstrumentModel> list = null;
			instrumentRepository.deleteAll();
			list= DateFormatUtil.convertInstrumentModel(
					tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken).getInstruments());
			instrumentRepository.addAllInstruments(list);
			
			if (list != null && list.size() > 0) {
				for (InstrumentModel instrument : list) {
					this.cacheInstrumentTradingSymbol.put(instrument.getInstrumentToken(), instrument.getTradingSymbol());
					cacheStopLossValue.put(instrument.getInstrumentToken(), 5.00);
					cacheQuantity.put(instrument.getInstrumentToken(), 2);
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

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{calculateStopLossFlag}")
	public void startLiveTrade(@PathVariable("userId") String userId, @PathVariable("requestToken") String requestToken,@PathVariable("calculateStopLossFlag") Boolean trailStopLossFlag,
			@RequestBody ArrayList<Long> instrumentTokens) throws ParseException {
		tradePortZerodhaConnect.setCalculateStopLossFlag(trailStopLossFlag);
		try {
			tradePortZerodhaConnect.setBackTestFlag(true);
			
			KiteConnect kiteconnect = tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken);
			this.cacheUserSession.put(userId, kiteconnect);
			
			List<InstrumentModel> list = null;
			instrumentRepository.deleteAll();
			list= DateFormatUtil.convertInstrumentModel(
					tradePortZerodhaConnect.getKiteConnectSession(userId,requestToken).getInstruments());
			instrumentRepository.addAllInstruments(list);
			
			
			if (list != null && list.size() > 0) {
				for (InstrumentModel instrument : list) {
					this.cacheInstrumentTradingSymbol.put(instrument.getInstrumentToken(), instrument.getTradingSymbol());
				}
			}

			if (instrumentTokens != null && instrumentTokens.size() > 0) {
				for (Iterator<Long> iterator = instrumentTokens.iterator(); iterator.hasNext();) {
					Long long1 = (Long) iterator.next();
					this.cacheMaxTrailStopLoss.put(long1, 0.0);
					this.cacheTrailStopLossSignal.put(long1, false);
					cacheStopLossValue.put(long1, 30.00);
					this.cacheTargetPrice.put(long1, 30.00);
					this.cacheQuantity.put(long1, 5);
					startTrade.put(long1, false);
				}
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long dayFriction = 86400000L;
			Date historicalToDate = dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis())));
			Date historicalFromDate = dateFormat
					.parse(dateFormat.format(new Date(historicalToDate.getTime() - (dayFriction*2))));
			tradePortZerodhaConnect.startBackTesting(kiteconnect, instrumentTokens, historicalFromDate,
					historicalToDate, "5minute", false);
			
			Map<String, List<Position>> map=tradePortZerodhaConnect.getPositions(userId, requestToken);
			if(map!=null) {
				for (Map.Entry<String, List<Position>> entry : map.entrySet()) {
					if(entry.getKey().equalsIgnoreCase("net")) {
						List<Position> positionList=entry.getValue();
						if(positionList!=null && positionList.size()>0) {
							for(int i=0;i<positionList.size();i++) {
								Position position=positionList.get(i);
								System.out.println(position.instrumentToken+" Started: "+position.netQuantity+" Total profit loss: "+position.pnl);
								if(position.netQuantity>0) {
									instrumentTokens.add(Long.parseLong(position.instrumentToken));
									System.out.println(position.instrumentToken+" Started: "+position.netQuantity);
								}
							}
						}
					}
				}
			}else {
				System.out.println("open Position is null....");
			}
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
