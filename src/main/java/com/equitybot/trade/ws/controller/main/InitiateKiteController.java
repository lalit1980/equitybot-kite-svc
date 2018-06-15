package com.equitybot.trade.ws.controller.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.TimeSeries;

import com.equitybot.trade.util.DateFormatUtil;
import com.equitybot.trade.ws.service.kite.TradePortZerodhaConnect;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;

@RestController
@RequestMapping("/api")
public class InitiateKiteController {

	@Autowired
	private TradePortZerodhaConnect tradePortZerodhaConnect;

	@GetMapping("/process/v1.0/{userId}/{requestToken}")
	public void getKiteConnectSession(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken) {
		try {
			tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken);
		} catch (JSONException | IOException | KiteException e) {
		}
	}

	@GetMapping("/process/v1.0/{userId}/{requestToken}/{fromDate}/{toDate}/{instrumentToken}/{interval}/{continuous}")
	public HistoricalData getHistoricalData(@PathVariable("userId") String userId,
								  @PathVariable("requestToken") String requestToken,
								  @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate ,
								  @PathVariable("toDate")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
								  @PathVariable("instrumentToken") String instrumentToken,
								  @PathVariable("interval") String interval,
								  @PathVariable("continuous") boolean continuous) {
		HistoricalData historicalData=null;
		try {
			
			KiteConnect kiteconnect=tradePortZerodhaConnect.getKiteConnectSession("XI3306", requestToken);
			historicalData=kiteconnect.getHistoricalData(fromDate, toDate, instrumentToken, interval, continuous);
			DateFormatUtil.loadHistoricalDataSeries(historicalData, instrumentToken);
			return historicalData;
		} catch (JSONException | IOException | KiteException e) {
		}
		return historicalData;
	}

	@PostMapping("/process/v1.0/{userId}/{requestToken}/{backTestingFlag}")
	public void startTick(@PathVariable("userId") String userId, 
						  @PathVariable("requestToken") String requestToken,
						  @PathVariable("backTestingFlag") boolean backTestingFlag,
			              @RequestBody ArrayList<Long> instrumentTokens) {
		if(backTestingFlag) {
			tradePortZerodhaConnect.startBackTesting(instrumentTokens);
		}else {
			try {
				tradePortZerodhaConnect.tickerUsage(tradePortZerodhaConnect.getKiteConnectSession(userId, requestToken),instrumentTokens);
			} catch (IOException | WebSocketException | KiteException e) {
				e.printStackTrace();
			}	
		}
		
	}

}
