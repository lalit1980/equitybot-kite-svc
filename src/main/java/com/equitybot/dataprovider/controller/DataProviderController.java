package com.equitybot.dataprovider.controller;

import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.service.CSVDataProvider;
import com.equitybot.dataprovider.service.HistoricalDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dataprovider/v1.0")
public class DataProviderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private HistoricalDataService historicalDataService;

    @Autowired
    private CSVDataProvider csvDataProvider;

    @PostMapping("/live/tiger")
    public void tigerLivelData(
            @RequestBody List<Long> instrumentTokens) {

        logger.info(" -- tigerHistoricalData");
      //  tiger(fromDate, toDate, instrumentTokens, interval, false);
    }


    @PostMapping("/historicalData/tiger")
    public void tigerHistoricalData(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
            @RequestBody List<Long> instrumentTokens, @RequestParam("interval") String interval) {

        logger.info(" -- tigerHistoricalData");
        tiger(fromDate, toDate, instrumentTokens, interval, false);
    }

    private void tiger(Date fromDate, Date toDate, List<Long> instrumentTokens, String interval, boolean continuous) {
        List<CompletableFuture<TickDTO>> completableFutures = new ArrayList<>();

        for (Long instrument : instrumentTokens) {
            completableFutures.add(historicalDataService.serve(instrument, fromDate, toDate, interval, continuous));
        }
        CompletableFuture<TickDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
    }

    @GetMapping("/csv/tiger")
    public void tigerCSVData(
            @RequestParam("filePath") String inputFile,
            @RequestParam("instrument") String instrument) throws IOException, ParseException {
        csvDataProvider.serve(Long.parseLong(instrument), inputFile);
        logger.info(" -- tiger CSV Data");
    }


}
