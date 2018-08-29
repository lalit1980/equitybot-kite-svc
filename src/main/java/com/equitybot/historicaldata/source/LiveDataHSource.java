package com.equitybot.historicaldata.source;

import com.equitybot.historicaldata.service.HistoricalLiveDataService;
import com.equitybot.kite.KiteConnection;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public final class LiveDataHSource implements OnConnect, OnDisconnect, OnOrderUpdate, OnError, OnTicks {

   private KiteTicker tickerProvider;

    @Autowired
    private KiteConnection kiteConnection;

    @Autowired
    private HistoricalLiveDataService historicalLiveDataService;

    private static boolean on;

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
        // your code goes here
    }

    @Override
    public void onOrderUpdate(Order order) {
        //logger.info("order update {}", order.orderId);
    }

    @Override
    public void onError(Exception exception) {
        // handle here.
    }

    @Override
    public void onError(KiteException kiteException) {
        // handle here.
    }

    @Override
    public void onTicks(ArrayList<Tick> ticks) {
        if (ticks != null && !ticks.isEmpty() && ticks.get(0).getMode() != null) {
               // historicalLiveDataService.serve();
        }
    }

    public void initConnect(KiteConnect kiteConnect) throws KiteException {
        tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());
        tickerProvider.setOnConnectedListener(this);
        tickerProvider.setOnDisconnectedListener(this);
        tickerProvider.setOnOrderUpdateListener(this);
        tickerProvider.setOnErrorListener(this);
        tickerProvider.setOnTickerArrivalListener(this);
        tickerProvider.setTryReconnection(true);
        tickerProvider.setMaximumRetries(10);
        tickerProvider.setMaximumRetryInterval(30);
        tickerProvider.connect();
    }

    public void subscribe(ArrayList<Long> instruments) throws KiteException {
        if (this.tickerProvider == null && !tickerProvider.isConnectionOpen()) {
            initConnect(kiteConnection.session());
        }
        tickerProvider.subscribe(instruments);
        tickerProvider.setMode(instruments, KiteTicker.modeFull);
        on = true;
    }

    public void disconnect() {
        tickerProvider.disconnect();
    }

    public boolean isConnectionOpen() {
        return tickerProvider.isConnectionOpen();
    }

    public void unsubscribe(ArrayList<Long> instruments) {
        tickerProvider.unsubscribe(instruments);
    }

    public static boolean isOn() {
        return on;
    }
}
