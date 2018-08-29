package com.equitybot.dataprovider.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.util.DataMapper;
import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class LiveDataService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataProviderService dataProviderService;

    @Async("dataProviderTaskPool")
    public CompletableFuture<TickDTO> serve(Tick tick, boolean isLive) {
        TickDTO tickDTO = DataMapper.mapInTick(tick);
        tickDTO.setDummyData(isLive);
        logger.info(" Tick Received : {}", tickDTO);
        dataProviderService.serve(tickDTO);
        return CompletableFuture.completedFuture(tickDTO);
    }
}
