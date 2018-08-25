package com.equitybot.historicaldata.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.historicaldata.source.HistoricalDataHSource;
import com.equitybot.historicaldata.utill.DataMapper;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class HistoricalDataHService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataProviderHService dataProviderHService;

    @Autowired
    private HistoricalDataHSource historicalDataHSource;

    @Async("dataProviderTaskPool")
    public CompletableFuture<TickDTO> serve(Long instrument, Date fromDate, Date toDate, String interval,
                                            boolean continuous, boolean dummyData) {
        TickDTO tickDTO = null;

        try {
            String name = Thread.currentThread().getName();
            System.out.println(" -- instrument : " + instrument + " # Thread Name : " + name);

            HistoricalData historicalDatas = historicalDataHSource.getHistoricalData(instrument, fromDate, toDate, interval, continuous);
            if (historicalDatas != null && !historicalDatas.dataArrayList.isEmpty()) {
                for (HistoricalData historicalData : historicalDatas.dataArrayList) {
                    tickDTO = DataMapper.mapInTick(historicalData, instrument);
                    tickDTO.setDummyData(dummyData);
                    logger.info(" Tick Received : {}", tickDTO);
                    dataProviderHService.serve(tickDTO);
                }
            }
        } catch (ParseException | KiteException | IOException e) {
            logger.error("error : ", e);
        }
        return CompletableFuture.completedFuture(tickDTO);

    }

}
