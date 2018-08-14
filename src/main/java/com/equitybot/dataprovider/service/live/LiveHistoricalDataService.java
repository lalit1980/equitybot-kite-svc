package com.equitybot.dataprovider.service.live;

import com.equitybot.common.model.TickDTO;
import com.zerodhatech.models.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LiveHistoricalDataService {


    @Autowired
    private HOLCLiveHistoricalDataService holcLiveHistoricalDataService;

    @Async("dataProviderTaskPool")
    public CompletableFuture<Long> serve(Long instrument, Map<Integer, HistoricalData> historicalDataMap) {
        List<CompletableFuture<Long>> completableFutures = new ArrayList<>();
        for (Map.Entry<Integer, HistoricalData> pair : historicalDataMap.entrySet()) {
            completableFutures.add(holcLiveHistoricalDataService.serve(instrument, pair.getKey(), pair.getValue()));
        }
        CompletableFuture<TickDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
        return CompletableFuture.completedFuture(instrument);
    }


}
