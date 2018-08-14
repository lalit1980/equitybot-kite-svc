package com.equitybot.dataprovider.service;

import com.equitybot.common.config.YAMLConfig;
import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.service.live.LiveHistoricalDataService;
import com.equitybot.dataprovider.source.HistoricalDataSource;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LiveDataPreparationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private DataProviderService dataProviderService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private HistoricalDataSource historicalDataSource;

    @Value("${historicaldata.day}")
    private Long historiCaldataDay;

    @Autowired
    private LiveHistoricalDataService liveHistoricalDataService;
    private volatile boolean hasMatch = false;
    private volatile boolean init = false;


    private List<List<Tick>> currentTicklList = Collections.synchronizedList(new ArrayList<>());

    private Map<Long, Map<Integer, HistoricalData>> getHistoticalData(List<Tick> ticks, List<Long> instruments)
            throws KiteException, IOException {
        Map<Long, Map<Integer, HistoricalData>> historicalDataMap = null;
        Long dayFriction = 86400000L;
        Date date = ticks.get(0).getTickTimestamp();
        int second = date.getSeconds();
        if (second == 0) {
            currentTicklList.add(ticks);
            hasMatch = true;
            historicalDataMap = new ConcurrentHashMap<>();
            Date historicalToDate = new Date(date.getTime());
            Date historicalFromDate = new Date(date.getTime() - (dayFriction * historiCaldataDay));
            for (Long instrument : instruments) {
                Map<Integer, HistoricalData> instrumentMap = new ConcurrentHashMap<>();
                historicalDataMap.put(instrument, instrumentMap);
                for (int barSize : yamlConfig.getBarsizesValue()) {
                    instrumentMap.put(barSize, historicalDataSource.getHistoricalData(instrument, historicalFromDate,
                            historicalToDate, barSize + "minute", true));
                }
            }
        }
        return historicalDataMap;
    }

    public void serve(ArrayList<Tick> ticks, List<Long> instruments) throws KiteException, IOException {
        if (!init) {
            if (hasMatch) {
                currentTicklList.add(ticks);
            } else {
                Map<Long, Map<Integer, HistoricalData>> historicalDataMap = getHistoticalData(ticks, instruments);
                if (historicalDataMap != null) {
                    processHistoricalData(historicalDataMap);
                }
            }
        } else {

        }
    }

    private void processHistoricalData(Map<Long, Map<Integer, HistoricalData>> historicalDataMap) {
        List<CompletableFuture<Long>> completableFutures = new ArrayList<>();
        for (Map.Entry<Long, Map<Integer, HistoricalData>> pair : historicalDataMap.entrySet()) {
            completableFutures.add(liveHistoricalDataService.serve(pair.getKey(), pair.getValue()));
        }
        CompletableFuture<TickDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
    }
}
