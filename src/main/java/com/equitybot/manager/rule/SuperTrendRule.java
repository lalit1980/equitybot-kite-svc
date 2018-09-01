package com.equitybot.manager.rule;

import com.equitybot.common.model.BarDTO;
import com.equitybot.common.model.TickDTO;
import com.equitybot.common.util.Util;

public class SuperTrendRule {

    public static String sevenByThreeInOneMinuteRule(TickDTO tickDTO){
        BarDTO barDTO = tickDTO.getBarDTOS().get(Util.barName(tickDTO.getInstrumentToken(),1));
        return  barDTO != null ?
                (barDTO.getSuperTrends().get(Util.superTrendName(barDTO.getInstrument(),barDTO.getBarSize(),3,7)) != null
                ? barDTO.getSuperTrends().get(Util.superTrendName(barDTO.getInstrument(),barDTO.getBarSize(),3,7)).getBuySell():null)
                : null;
    }
}
