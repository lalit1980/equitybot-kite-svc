package com.equitybot.historicaldata.service;

import com.equitybot.algorithm.service.AlgorithmService;
import com.equitybot.common.model.TickDTO;
import com.equitybot.manager.service.TradeManagerService;
import com.equitybot.reporting.service.SuperTrendReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataProviderHService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HLOCBarHService hlocBarService;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private SuperTrendReportingService superTrendReportingService;

    @Autowired
    private TradeManagerService tradeManagerService;


    public void serve(TickDTO tickDTO) {
        System.out.println("______________________ Tick _______________________________");
        try {
            hlocBarService.serve(tickDTO);
            if (!tickDTO.getBarDTOS().isEmpty()) {
                algorithmService.service(tickDTO);
                tradeManagerService.serve(tickDTO);
            }
            superTrendReportingService.saveRecord(tickDTO);
        } catch (IOException e) {
            logger.error("error : ", e);
        }
        System.out.println("_____________________________________________________");
    }
}
