package com.equitybot.manager.service;

import com.equitybot.common.model.TickDTO;
import org.springframework.stereotype.Service;

@Service
public class TradeManagerService {

    public void serve(TickDTO tickDTO){
        if(!tickDTO.isDummyData()){
            // add code here
        }
    }
}
