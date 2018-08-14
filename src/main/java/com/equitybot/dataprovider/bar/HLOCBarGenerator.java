package com.equitybot.dataprovider.bar;

import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.cache.DataProviderCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.ta4j.core.Decimal;

import java.util.concurrent.CompletableFuture;

@Service
public class HLOCBarGenerator {

    @Autowired
    private DataProviderCache dataProviderCache;

    public static void act(BarModel barModel, TickDTO tick) {
        if (barModel.tickCount == 0) {
            barModel.lowPrice = Decimal.valueOf(tick.getLowPrice());
            barModel.highPrice = Decimal.valueOf(tick.getHighPrice());
            barModel.openPrice = Decimal.valueOf(tick.getOpenPrice());
            barModel.volume = Decimal.valueOf(tick.getLastTradedQuantity());
        } else {
            barModel.lowPrice = barModel.lowPrice.isGreaterThan(tick.getLowPrice()) ?
                    Decimal.valueOf(tick.getLastTradedPrice()) : barModel.lowPrice;
            barModel.highPrice = barModel.highPrice.isLessThan(tick.getHighPrice()) ?
                    Decimal.valueOf(tick.getLastTradedPrice()) : barModel.highPrice;
            barModel.volume = barModel.volume.plus(tick.getLastTradedQuantity());
        }
        barModel.closePrice = Decimal.valueOf(tick.getClosePrice());
        barModel.timestamp = tick.getTickTimestamp();
        barModel.tickCount++;
    }

    public static BarDTO getBarDTOS(BarModel barModel) {
        return new BarDTO(barModel.getInstrument(), barModel.getBarSize(), barModel.getHighPrice(), barModel.getLowPrice(),
                barModel.getOpenPrice(), barModel.getClosePrice(), barModel.getVolume(), barModel.getTimestamp());
    }

    @Async("hlocTaskPool")
    public CompletableFuture<BarModel> generateAsync(int barSize, TickDTO tickDTO) {
        String name = Thread.currentThread().getName();
        System.out.println(" -- instrument : " + tickDTO.getInstrumentToken() + " # Thread Name : " + name +
                "  # barSize : " + barSize);

        String instrumentAndBarSize = tickDTO.getInstrumentToken() + "-" + barSize;
        BarModel barModel = dataProviderCache.getTickDataModel(instrumentAndBarSize);
        if (barModel == null) {
            barModel = getNewTickDataBarModel(tickDTO.getInstrumentToken(), barSize);
        }
        act(barModel, tickDTO);
        if (barModel.getTickCount() == barSize) {
            tickDTO.getBarDTOS().add(getBarDTOS(barModel));
            dataProviderCache.putOnTickDataModelCache(instrumentAndBarSize, getNewTickDataBarModel(tickDTO.getInstrumentToken(), barSize));
            return CompletableFuture.completedFuture(barModel);

        } else {
            dataProviderCache.putOnTickDataModelCache(instrumentAndBarSize, barModel);
            return CompletableFuture.completedFuture(null);
        }
    }

    private BarModel getNewTickDataBarModel(Long instrument, int barSize) {
        return new BarModel(instrument, barSize);
    }
}
