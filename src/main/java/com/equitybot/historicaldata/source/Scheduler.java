package com.equitybot.historicaldata.source;

import com.equitybot.historicaldata.service.HistoricalLiveDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class Scheduler {




    @Autowired
    private HistoricalLiveDataService historicalLiveDataService;

    private Date lastRecordTime;


    public void scheduler(){
        final long time = this.historicalLiveDataService.getCurrentPerfectMinuteTime();
        if( time - lastRecordTime.getTime() > 60000){
            historicalLiveDataService.serve();
        }
    }
}
