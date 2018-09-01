package com.equitybot.reporting.service;

import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.SuperTrendDTO;
import com.equitybot.common.model.TickDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SuperTrendReportingService {

    private Map<String, File> fileMap = new ConcurrentHashMap<>();

    @Value("${report.outputFolder}")
    private String outputFolder;

    public void saveRecord(TickDTO tick) throws IOException {

        StringBuilder result;
        for (BarDTO bar : tick.getBarDTOS().values()) {
            for (SuperTrendDTO superTrend : bar.getSuperTrends().values()) {
                result = new StringBuilder();
                result.append(bar.getTimestamp());
                result.append(",");
                result.append(bar.getOpenPrice());
                result.append(",");
                result.append(bar.getHighPrice());
                result.append(",");
                result.append(bar.getLowPrice());
                result.append(",");
                result.append(bar.getClosePrice());
                result.append(",");
                result.append(superTrend.getTrueRange());
                result.append(",");
                result.append(superTrend.getAverageTrueRange());
                result.append(",");
                result.append(superTrend.getBasicUpperBand());
                result.append(",");
                result.append(superTrend.getBasicLowerBand());
                result.append(",");
                result.append(superTrend.getFinalUpperBand());
                result.append(",");
                result.append(superTrend.getFinalLowerBand());
                result.append(",");
                result.append(superTrend.getSuperTrend());
                result.append(",");
                result.append(superTrend.getBuySell());
                save(result.toString(), getFile(superTrend));
            }
        }
    }


    private void save(String csvRow, String absolutePath) throws IOException {
        Files.write(Paths.get(absolutePath), csvRow.getBytes(), StandardOpenOption.APPEND);
        Files.write(Paths.get(absolutePath), System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);
    }

    private String getFile(SuperTrendDTO superTrendDTO) throws IOException {

        String fileKey = getKey(superTrendDTO);
        if (fileMap.containsKey(fileKey)) {
            return fileMap.get(fileKey).getAbsolutePath();
        } else {
            File file = new File(outputFolder + fileKey + "_" + System.currentTimeMillis() + ".csv");
            file.createNewFile();
            fileMap.put(fileKey, file);
            save("Date" + "," + "Open" + "," + "High" + "," + "Low" + "," + "Close" + "," + "True Range" + "," + "Avg True Range" + "," +
                            "Basic Upper Band" + "," + "Basic Lower Band" + "," + "Final Upper Band" + "," + "Final Lower Band" +
                            "," + "Super Trend"
                    , file.getAbsolutePath());
            return file.getAbsolutePath();
        }
    }

    private String getKey(SuperTrendDTO superTrendDTO) {
        return superTrendDTO.getInstrument() + "_" +
                superTrendDTO.getBarSize() + "_" +
                superTrendDTO.getMultiplier() + "_" +
                superTrendDTO.getPeriod();
    }

}
