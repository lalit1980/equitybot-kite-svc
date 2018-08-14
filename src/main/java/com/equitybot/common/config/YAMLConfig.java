package com.equitybot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private List<String> barsizesminutes = Collections.synchronizedList(new ArrayList<>());

    private List<Integer> barsizesValue;

    private List<String> multiplierPeriods = Collections.synchronizedList(new ArrayList<>());

    private List<int[]> multiplierPeriodValue;

    public List<String> getBarsizesminutes() {
        return barsizesminutes;
    }

    public void setBarsizesminutes(List<String> barsizesminutes) {
        this.barsizesminutes = barsizesminutes;
    }

    public List<Integer> getBarsizesValue() {
        if (barsizesValue == null) {
            barsizesValue = Collections.synchronizedList(new ArrayList<>());
            for (String barsize : barsizesminutes) {
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
            multiplierPeriodValue = Collections.synchronizedList(new ArrayList<>());
            for (String multiplierPeriod : multiplierPeriods) {
                String[] valueString = multiplierPeriod.split(",");
                multiplierPeriodValue.add(new int[]{Integer.parseInt(valueString[0]), Integer.parseInt(valueString[1])});
            }
        }
        return multiplierPeriodValue;
    }


}
