package com.equitybot.algorithm.service;

import com.equitybot.common.model.TickDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {

    @Autowired
    private SuperTrendService superTrendService;

    public void service(TickDTO tick) {
        superTrendService.serve(tick);
    }
}