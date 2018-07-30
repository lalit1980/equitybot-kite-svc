package com.equitybot.trade.converter;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;


import com.equitybot.trade.db.mongodb.tick.domain.Tick;
import com.equitybot.trade.ignite.configs.IgniteConfig;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomTickBarList {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<Long, CustomTickBar> workingTickBarMap;
	private Map<Long, TimeSeries> timeSeriesMap;

	@Value("${tick.barsize}")
	private long eachBarSize;

	@Value("${spring.kafka.producer.topic-data-seriesupdate}")
	private String timeSeriesProducerTopic;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	IgniteConfig igniteConfig;

	IgniteCache<String, TimeSeries> cache;

	public CustomTickBarList() {
		this.workingTickBarMap = new HashMap<>();
		this.timeSeriesMap = new HashMap<>();
		
		CacheConfiguration<String, TimeSeries> ccfg = new CacheConfiguration<String, TimeSeries>("TimeSeriesCache");
		ccfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		ccfg.setCacheMode(CacheMode.PARTITIONED);
		ccfg.setRebalanceMode(CacheRebalanceMode.NONE);
		ccfg.setDataRegionName("1GB_Region");
		cache = igniteConfig.getInstance().getOrCreateCache(ccfg);
	}

	public synchronized void addTick(Tick tick) {
		CustomTickBar customTickBar = workingTickBarMap.get(tick.getInstrumentToken());
		if (customTickBar == null) {
			logger.info("Each Bar Size in: "+ this.eachBarSize);
			customTickBar = new CustomTickBar(tick.getInstrumentToken(), this.eachBarSize);
			workingTickBarMap.put(tick.getInstrumentToken(), customTickBar);
		}
		customTickBar.addTick(tick);
		this.getWorkingMinuteTickListValidate(customTickBar);
	}

	public void getWorkingMinuteTickListValidate(CustomTickBar customTickBar) {
		if (customTickBar.getListCount() == this.eachBarSize) {
			CustomTickBar newCustomTickBar = new CustomTickBar(customTickBar.getInstrumentToken(), this.eachBarSize);
			this.addInSeries(customTickBar);
			this.workingTickBarMap.put(newCustomTickBar.getInstrumentToken(), newCustomTickBar);
		}
	}

	private void addInSeries(CustomTickBar customTickBar) {
		Duration barDuration = Duration.ofSeconds(60);

		Bar bar = new BaseBar(barDuration, customTickBar.getEndTime(), Decimal.valueOf(customTickBar.getOpenPrice()),
				Decimal.valueOf(customTickBar.getHighPrice()), Decimal.valueOf(customTickBar.getLowPrice()),
				Decimal.valueOf(customTickBar.getClosePrice()), Decimal.valueOf(customTickBar.getVolume()),
				Decimal.valueOf(customTickBar.getVolume()));
		bar.addTrade(customTickBar.getVolume(), customTickBar.getClosePrice());
		TimeSeries timeSeries = this.timeSeriesMap.get(customTickBar.getInstrumentToken());
		if (timeSeries == null) {
			timeSeries = new BaseTimeSeries(String.valueOf(customTickBar.getInstrumentToken()));
			this.timeSeriesMap.put(customTickBar.getInstrumentToken(), timeSeries);
		}
		timeSeries.addBar(bar);
		logger.info("Bar Count in series: "+timeSeries.getBarCount());
		cache.put(timeSeries.getName(), timeSeries);
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(timeSeriesProducerTopic,timeSeries.getName());
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				logger.info("Sent message: " + result);
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("Failed to send message");
			}
		});
	}

	public TimeSeries getSeries(Long instrumentToken) {
		return this.timeSeriesMap.get(instrumentToken);
	}

	public IgniteCache<String, TimeSeries> getCache() {
		return cache;
	}

	public void backTest(Tick tick) {
		Duration barDuration = Duration.ofSeconds(60);
		ZoneId istZone = ZoneId.of("Asia/Kolkata");
		if (true) {
			ZonedDateTime barEndTime = ZonedDateTime.ofInstant(tick.getTickTimestamp().toInstant(), istZone);
			
			Bar bar = new BaseBar(barDuration, barEndTime, Decimal.valueOf(tick.getOpenPrice()),
					Decimal.valueOf(tick.getHighPrice()), Decimal.valueOf(tick.getLowPrice()),
					Decimal.valueOf(tick.getClosePrice()), Decimal.valueOf(tick.getLastTradedQuantity()),
					Decimal.valueOf(tick.getLastTradedQuantity()));
			bar.addTrade(tick.getLastTradedQuantity(), tick.getLastTradedPrice());
			TimeSeries timeSeries = this.timeSeriesMap.get(tick.getInstrumentToken());
			if (timeSeries == null) {
				timeSeries = new BaseTimeSeries(String.valueOf(tick.getInstrumentToken()));
				this.timeSeriesMap.put(tick.getInstrumentToken(), timeSeries);
			}
			try {
			timeSeries.addBar(bar);
			}catch(IllegalArgumentException argumentException) {
				throw argumentException;
				
			}
			cache.put(timeSeries.getName(), timeSeries);
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(timeSeriesProducerTopic,
					timeSeries.getName());
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
				@Override
				public void onSuccess(SendResult<String, String> result) {
					//logger.info("Sent message: " + result);
				}

				@Override
				public void onFailure(Throwable ex) {
					logger.info("Failed to send message");
				}
			});
		}

	}

}