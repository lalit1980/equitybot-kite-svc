package com.equitybot.dataprovider.service.live;

import com.equitybot.algorithm.service.AlgorithmService;
import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.util.DataMapper;
import com.zerodhatech.models.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.ta4j.core.Decimal;

import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

@Service
public class HOLCLiveHistoricalDataService {

    @Autowired
    private AlgorithmService algorithmService;

    @Async("dataProviderTaskPool")
    public CompletableFuture<Long> serve(Long instrument, int barSize, HistoricalData historicalDatas) {
        if (historicalDatas != null && !historicalDatas.dataArrayList.isEmpty()) {
            for (HistoricalData historicalData : historicalDatas.dataArrayList) {
                TickDTO tickDTO = null;
                try {
                    tickDTO = DataMapper.mapInTick(historicalData, instrument);
                    updateBar(barSize, tickDTO);
                    algorithmService.service(tickDTO);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return CompletableFuture.completedFuture(instrument);
    }


    private void updateBar(int barSize, TickDTO tickDTO) {
        BarDTO barDTO = new BarDTO(tickDTO.getInstrumentToken(), barSize, Decimal.valueOf(tickDTO.getHighPrice()),
                Decimal.valueOf(tickDTO.getLowPrice()), Decimal.valueOf(tickDTO.getOpenPrice()),
                Decimal.valueOf(tickDTO.getClosePrice()), Decimal.valueOf(0), tickDTO.getTickTimestamp());
        tickDTO.getBarDTOS().add(barDTO);
    }
}
