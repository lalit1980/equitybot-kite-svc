package com.equitybot.algorithm.service;

import com.equitybot.algorithm.indicator.supertrend.SuperTrendIndicator;
import com.equitybot.common.config.YAMLConfig;
import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.SuperTrendDTO;
import com.equitybot.common.model.TickDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SuperTrendService {

    @Autowired
    private SuperTrendIndicator superTrendIndicator;

    @Autowired
    private YAMLConfig yamlConfig;


    public void serve(TickDTO tick) {
        List<CompletableFuture<SuperTrendDTO>> completableFutures = new ArrayList<>();
        CompletableFuture<SuperTrendDTO> trendDTOCompletableFuture;
        for (BarDTO bar : tick.getBarDTOS()) {
            for (int index = 0;yamlConfig.getMultiplierPeriodValue().size() > index ; index++) {
                int[] multiplierPeriod = yamlConfig.getMultiplierPeriodValue().get(index);
                trendDTOCompletableFuture = superTrendIndicator.generate(multiplierPeriod[0], multiplierPeriod[1], bar);
                completableFutures.add(trendDTOCompletableFuture);
            }
        }
        CompletableFuture<SuperTrendDTO>[] completableFutureArray = completableFutures
                .toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();
    }
}
