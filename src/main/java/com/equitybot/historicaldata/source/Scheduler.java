package com.equitybot.historicaldata.source;

import com.equitybot.historicaldata.service.HistoricalLiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {




    @Autowired
    private HistoricalLiveDataService historicalLiveDataService;

   private boolean flag=true;


    public void scheduler(){
        while(flag) {
                historicalLiveDataService.serve();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }

        }

}
