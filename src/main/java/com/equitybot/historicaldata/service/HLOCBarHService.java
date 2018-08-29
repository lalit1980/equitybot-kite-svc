package com.equitybot.historicaldata.service;

import com.equitybot.common.config.YAMLConfig;
import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.TickDTO;
import com.equitybot.historicaldata.bar.HLOCBarHGenerator;
import com.equitybot.historicaldata.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class HLOCBarHService {


    @Autowired
    private HLOCBarHGenerator hlocBarGenerator;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private Cache cache;


    public void serve(TickDTO tickDTO) {
        cache.putOnLatestTickCache(tickDTO.getInstrumentToken(), tickDTO);
        List<CompletableFuture<BarDTO>> completableFutures = new ArrayList<>();
        for (int barSize : yamlConfig.getBarsizesValue()) {
            completableFutures.add(hlocBarGenerator.generateAsync(barSize, tickDTO));
        }
        CompletableFuture<BarDTO>[] completableFutureArray = completableFutures.toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();

    }

}
