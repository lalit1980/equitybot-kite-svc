package com.equitybot.dataprovider.service;

import com.equitybot.algorithm.service.AlgorithmService;
import com.equitybot.common.model.TickDTO;
import com.equitybot.reporting.service.SuperTrendReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataProviderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HLOCBarService hlocBarService;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private SuperTrendReportingService superTrendReportingService;


    public void serve(TickDTO tickDTO) {
        try {
            hlocBarService.serve(tickDTO);
            if (!tickDTO.getBarDTOS().isEmpty()) {
                algorithmService.service(tickDTO);
            }
            superTrendReportingService.saveRecord(tickDTO);

        } catch (IOException e) {
            logger.error("error : ", e);
        }
    }
}
