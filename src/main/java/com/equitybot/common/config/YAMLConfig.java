package com.equitybot.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private List<String> barsizesminutes = Collections.synchronizedList(new ArrayList<>());

    private List<Integer> barsizesValue;

    private List<String> multiplierPeriods = Collections.synchronizedList(new ArrayList<>());

    private List<int[]> multiplierPeriodValue;

    private List<String> users = Collections.synchronizedList(new ArrayList<>());

    private Map<String, String[]> userValue;


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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public Map<String, String[]> getUsersValue() {
        if (userValue == null) {
            userValue = new ConcurrentHashMap<>();
            for (String user : users) {
                String[] valueString = user.split(",");
                userValue.put(valueString[0],valueString);
            }
        }
        return userValue;
    }
}
