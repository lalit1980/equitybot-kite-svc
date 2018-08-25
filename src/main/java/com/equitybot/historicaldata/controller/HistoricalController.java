package com.equitybot.historicaldata.controller;


import com.equitybot.historicaldata.service.HistoricalLiveDataService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/historical/v1.0")
public class HistoricalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HistoricalLiveDataService historicalLiveDataService;

    @PostMapping("/tiger")
    public void tiger(@RequestBody List<Long> instrumentTokens) throws KiteException {
        logger.info(" -- tiger");
        historicalLiveDataService.init(new ArrayList<>(instrumentTokens));
    }
}
