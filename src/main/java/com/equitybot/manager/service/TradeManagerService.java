package com.equitybot.manager.service;

import com.equitybot.common.model.TickDTO;

public class TradeManagerService {

    public void serve(TickDTO tickDTO){
        if(!tickDTO.isDummyData()){
            // add code here
        }
    }
}
