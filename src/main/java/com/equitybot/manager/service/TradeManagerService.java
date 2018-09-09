package com.equitybot.manager.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.manager.rule.SuperTrendRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TradeManagerService {
	
	@Autowired
	private PlaceOrderService placeOrderService;

    private static String activetule;

    public TradeManagerService() {
        this.activetule = "ST173";
    }

    @Async("superTrendTaskPool")
    public void serve(TickDTO tickDTO) {
        if (!tickDTO.isDummyData()) {
        	String action = execute(tickDTO);
            if( action != null ) {
            	placeOrderService.serve(tickDTO.getInstrumentToken(),action);
            }
        }
    }
    

    private String execute(TickDTO tickDTO) {
        switch (activetule) {
            case "ST173":
                return SuperTrendRule.sevenByThreeInOneMinuteRule(tickDTO);
            default:
                return null;
        }
    }

    public static String getActivetule() {
        return activetule;
    }

    public static void setActivetule(String activetule) {
        TradeManagerService.activetule = activetule;
    }
}
