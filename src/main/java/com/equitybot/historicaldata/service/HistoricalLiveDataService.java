package com.equitybot.historicaldata.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.historicaldata.source.HistoricalDataHSource;
import com.equitybot.historicaldata.source.LiveDataHSource;
import com.equitybot.historicaldata.source.Scheduler;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class HistoricalLiveDataService {

    @Value("${historicaldata.day}")
    private Long historiCaldataDay;

    @Autowired
    private HistoricalDataHSource historicalDataHSource;

    @Autowired
    private HistoricalDataHService historicalDataService;

    @Autowired
    private Scheduler scheduler;

   // @Autowired
    //private LiveDataHSource liveDataHSource;

    private List<Long> liveInstruments;

    private final Long dayFriction = 86400000L;

    private Date lastRecordTime;


    public void serve(){
        final long time = getCurrentPerfectMinuteTime();
        if( time - lastRecordTime.getTime() > 6000){
            processHistoricalData( liveInstruments, lastRecordTime , new Date (time) , true);
            lastRecordTime = new Date(time);
        }
    }


    public void init(ArrayList<Long> instruments) throws KiteException {
        long time = getCurrentPerfectMinuteTime();
        Date historicalToDate = new Date(time);
        Date historicalFromDate = new Date(time - (dayFriction * historiCaldataDay));
        processHistoricalData(instruments, historicalFromDate, historicalToDate, true);
        adjustTime( historicalToDate, instruments);
        this.liveInstruments = Collections.synchronizedList(instruments);
        scheduler.scheduler();
    }


    private void adjustTime(Date historicalToDate, List<Long> instruments){
        Date time = new Date(getCurrentPerfectMinuteTime());
        if(time.getTime()-historicalToDate.getTime() != 0){
            processHistoricalData(instruments, historicalToDate, time,true );
            adjustTime(time,instruments);
        }else{
            this.lastRecordTime = new Date(getCurrentPerfectMinuteTime());
        }
    }

    private void processHistoricalData(List<Long> instruments, Date historicalFromDate, Date historicalToDate
            , boolean dummyData ) {
        List<CompletableFuture<TickDTO>> completableFutures = new ArrayList<>();
        for (Long instrument : instruments) {
            completableFutures.add(historicalDataService.serve(instrument, historicalFromDate, historicalToDate,
                     "minute", false,dummyData));
        }
        CompletableFuture<TickDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
    }

    public static long getCurrentPerfectMinuteTime(){
        Date date = new Date(System.currentTimeMillis());
        int second = date.getSeconds();
        int mlSecond = (int)date.getTime()%1000;

        return date.getTime()-(second*1000)-mlSecond;
    }
}
