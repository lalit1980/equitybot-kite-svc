package com.equitybot.historicaldata.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.historicaldata.source.HistoricalDataHSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoricalLiveDataService {

    @Value("${historicaldata.day}")
    private Long historiCaldataDay;

    @Autowired
    private HistoricalDataHSource historicalDataHSource;

    @Autowired
    private HistoricalDataHService historicalDataService;

    private final Long dayFriction = 86400000L;


    public void init(List<Long> instruments) {

        Date date = new Date(System.currentTimeMillis());
        int second = date.getSeconds();
        long time = date.getTime()-(second*1000);
        Date historicalToDate = new Date(time);
        Date historicalFromDate = new Date(time - (dayFriction * historiCaldataDay));
        processHistoricalData(instruments, historicalFromDate, historicalToDate );
        adjustTime( historicalToDate, instruments);
    }


    private void adjustTime(Date historicalToDate, List<Long> instruments){
        Date date = new Date(System.currentTimeMillis());
        int second = date.getSeconds();
        long time = date.getTime()-(second*1000);
        if(time-historicalToDate.getTime() == 0){
            processHistoricalData(instruments, historicalToDate, new Date(time) );
            adjustTime(new Date(time),instruments);
        }
    }

    private void processHistoricalData(List<Long> instruments, Date historicalFromDate, Date historicalToDate ) {
        List<CompletableFuture<TickDTO>> completableFutures = new ArrayList<>();
        for (Long instrument : instruments) {
            completableFutures.add(historicalDataService.serve(instrument, historicalFromDate, historicalToDate,
                    1 + "minute", true));
        }
        CompletableFuture<TickDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
    }

}
