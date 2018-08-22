package com.equitybot.dataprovider.util;

import com.equitybot.common.model.DepthModel;
import com.equitybot.common.model.TickDTO;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Tick;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataMapper {

    public static final String KITE_TICK_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final DateFormat dateFormat;
    private static BoundMapperFacade<Tick, TickDTO> tickModelMapper;
    private static Map<String, ArrayList<DepthModel>> depth;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        tickModelMapper = mapperFactory.getMapperFacade(Tick.class, TickDTO.class);
        dateFormat = new SimpleDateFormat(KITE_TICK_TIMESTAMP_FORMAT, new Locale("en", "IN"));
        depth = getDepth();
    }

    private DataMapper() {
    }

    public static TickDTO mapInTick(Tick tick) {
        return tickModelMapper.map(tick);
    }

    public static TickDTO mapInTick(HistoricalData historicalData, Long instrument) throws ParseException {
        Date from = getDate(historicalData.timeStamp);
        TickDTO tick = new TickDTO();
        tick.setTickTimestamp(from);
        tick.setClosePrice(historicalData.close);
        tick.setOpenPrice(historicalData.open);
        tick.setHighPrice(historicalData.high);
        tick.setLowPrice(historicalData.low);
        tick.setLastTradedPrice(historicalData.close);
        tick.setLastTradedQuantity(historicalData.volume);
        tick.setInstrumentToken(instrument);
        tick.setDepth(depth);
        return tick;
    }

    public static TickDTO mapInTick(String csvRecord, Long instrument) throws ParseException {

        String[] hisString = csvRecord.split(",");

        TickDTO tick = new TickDTO();
        tick.setTickTimestamp(getDate(hisString[0]));
        tick.setOpenPrice(Double.parseDouble(hisString[1]));
        tick.setHighPrice(Double.parseDouble(hisString[2]));
        tick.setLowPrice(Double.parseDouble(hisString[3]));
        tick.setClosePrice(Double.parseDouble(hisString[4]));
        tick.setLastTradedQuantity(10);
        tick.setInstrumentToken(instrument);
        tick.setDepth(depth);
        return tick;
    }

    private static Map<String, ArrayList<DepthModel>> getDepth() {
        Map<String, ArrayList<DepthModel>> depths = new HashMap<>();
        ArrayList<DepthModel> buys = new ArrayList<>();
        ArrayList<DepthModel> sell = new ArrayList<>();
        DepthModel depth = new DepthModel();
        depth.setOrders(40);
        depth.setPrice(212.12);
        depth.setQuantity(100);
        buys.add(depth);
        depth = new DepthModel();
        depth.setOrders(40);
        depth.setPrice(218.12);
        depth.setQuantity(200);
        buys.add(depth);
        depth = new DepthModel();
        depth.setOrders(40);
        depth.setPrice(214.12);
        depth.setQuantity(300);
        buys.add(depth);
        depth = new DepthModel();
        depth.setOrders(40);
        depth.setPrice(290.12);
        depth.setQuantity(400);
        buys.add(depth);
        depths.put("buy", buys);
        depth = new DepthModel();
        depth.setOrders(30);
        depth.setPrice(3432.12);
        depth.setQuantity(4400);
        sell.add(depth);
        depth = new DepthModel();
        depth.setOrders(40);
        depth.setPrice(34.12);
        depth.setQuantity(200);
        sell.add(depth);
        depth = new DepthModel();
        depth.setOrders(50);
        depth.setPrice(324.12);
        depth.setQuantity(4500);
        sell.add(depth);
        depth = new DepthModel();
        depth.setOrders(60);
        depth.setPrice(53.12);
        depth.setQuantity(453);
        sell.add(depth);
        depths.put("sell", sell);
        return depths;

    }

    private static Date getDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }

}
