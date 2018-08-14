package com.equitybot.dataprovider.model;

import com.equitybot.common.model.TickDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoricalTimeMatch {

    private Map<Long, Integer> timeIndexMap;
    private List<TickDTO> tickDTOS;

    public HistoricalTimeMatch() {
        this.timeIndexMap = new ConcurrentHashMap<>();
        this.tickDTOS = Collections.synchronizedList(new ArrayList<>());
    }

    public Map<Long, Integer> getTimeIndexMap() {
        return timeIndexMap;
    }

    public void setTimeIndexMap(Map<Long, Integer> timeIndexMap) {
        this.timeIndexMap = timeIndexMap;
    }

    public List<TickDTO> getTickDTOS() {
        return tickDTOS;
    }

    public void setTickDTOS(List<TickDTO> tickDTOS) {
        this.tickDTOS = tickDTOS;
    }
}
