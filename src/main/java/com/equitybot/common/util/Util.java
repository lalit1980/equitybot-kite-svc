package com.equitybot.common.util;

public class Util {

    public static final String SUPERTREND_NAME_FORMAT = "instrument:%s#barSize:%s#multiplier:%s#period:%s";

    public static final String BAR_NAME_FORMAT = "instrument:%s#barSize:%s";

    public static String superTrendName(long instrument, int barSize, int multiplier, int period){
        return String.format(SUPERTREND_NAME_FORMAT, instrument, barSize, multiplier, period);
    }

    public static String  barName(long instrument, int barSize){
        return String.format(BAR_NAME_FORMAT, instrument, barSize);
    }
}
