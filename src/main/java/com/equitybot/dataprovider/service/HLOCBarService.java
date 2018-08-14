package com.equitybot.dataprovider.service;

import com.equitybot.common.config.YAMLConfig;
import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.bar.HLOCBarGenerator;
import com.equitybot.dataprovider.cache.DataProviderCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class HLOCBarService {


    @Autowired
    private HLOCBarGenerator hlocBarGenerator;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private DataProviderCache dataProviderCache;


    public void serve(TickDTO tickDTO) {
        dataProviderCache.putOnLatestTickCache(tickDTO.getInstrumentToken(), tickDTO);
        List<CompletableFuture<BarDTO>> completableFutures = new ArrayList<>();
        for (int barSize : yamlConfig.getBarsizesValue()) {
            completableFutures.add(hlocBarGenerator.generateAsync(barSize * 60, tickDTO));
        }
        CompletableFuture<BarDTO>[] completableFutureArray = completableFutures.toArray(new CompletableFuture[completableFutures.size()]);
        CompletableFuture.allOf(completableFutureArray).join();

    }

}
