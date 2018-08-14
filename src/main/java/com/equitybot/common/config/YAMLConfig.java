package com.equitybot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private List<String> barsizes = new ArrayList<>();

    private List<Integer> barsizesValue;

    private List<String> multiplierPeriods = new ArrayList<>();

    private List<int[]> multiplierPeriodValue;

    public List<String> getBarsizes() {
        return barsizes;
    }

    public void setBarsizes(List<String> barsizes) {
        this.barsizes = barsizes;
    }

    public List<Integer> getBarsizesValue() {
        if (barsizesValue == null) {
            barsizesValue = new ArrayList<>();
            for (String barsize : barsizes) {
                barsizesValue.add(Integer.parseInt(barsize));
            }
        }
        return barsizesValue;
    }

    public List<String> getMultiplierPeriods() {
        return multiplierPeriods;
    }

    public void setMultiplierPeriods(List<String> multiplierPeriods) {
        this.multiplierPeriods = multiplierPeriods;
    }

    public List<int[]> getMultiplierPeriodValue() {
        if (multiplierPeriodValue == null) {
            multiplierPeriodValue = new ArrayList<>();
            for (String multiplierPeriod : multiplierPeriods) {
                String[] valueString = multiplierPeriod.split(",");
                multiplierPeriodValue.add(new int[]{Integer.parseInt(valueString[0]), Integer.parseInt(valueString[1])});
            }
        }
        return multiplierPeriodValue;
    }


}
