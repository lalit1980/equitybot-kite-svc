package com.equitybot.dataprovider.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.model.HistoricalTimeMatch;
import com.equitybot.dataprovider.util.DataMapper;
import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LiveDataService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Map<Long,HistoricalTimeMatch> historicalTimeMatchMap = new ConcurrentHashMap<>();

    @Autowired
    private DataProviderService dataProviderService;

    @Async("dataProviderTaskPool")
    public void serve(Tick tickData) {
        TickDTO tick = DataMapper.mapInTickModel(tickData);

        HistoricalTimeMatch historicalTimeMatch = historicalTimeMatchMap.get(tick.getInstrumentToken());
        if(historicalTimeMatch ==null){
            historicalTimeMatch = new HistoricalTimeMatch();
            historicalTimeMatchMap.put(tick.getInstrumentToken(),historicalTimeMatch);
        }





        logger.info(" Tick Received : {}", tick);
        dataProviderService.serve(tick);
    }

}
