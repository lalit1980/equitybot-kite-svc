package com.equitybot.trade.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.zerodhatech.models.Depth;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Tick;


public class DateFormatUtil {
	public static final String KITE_SERVICE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	public static final String KITE_TICK_TIMESTAMP_FORMAT = "MMM dd, yyyy hh:mm:ss a";

	public static final String MONGODB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String TA4J = "yyyy-MM-dd";
	public static final String IST_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

	public static String toISO8601UTC(Date date, String dateFormat) {

		Format formatter = new SimpleDateFormat(MONGODB_DATE_FORMAT);
		return formatter.format(date);
	}
	public static String  getCurrentISTTime() {
		Calendar currentdate = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		TimeZone obj = TimeZone.getTimeZone("IST");
		formatter.setTimeZone(obj);
		return formatter.format(currentdate.getTime());
    }
	public static String  getCurrentISTTime(Date date) {
		Calendar currentdate = Calendar.getInstance();
		currentdate.setTime(date);
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		TimeZone obj = TimeZone.getTimeZone("IST");
		formatter.setTimeZone(obj);
		return formatter.format(currentdate.getTime());
    }
	public static void test() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
		 String dateInString = "2018-08-03T15:29:00+0530";
		 try {
	            Date date1 = formatter.parse(dateInString);
	            System.out.println(date1);
	            System.out.println(formatter.format(date1));

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	}
	public static void main(String[] args) {
		getCurrentISTTime(new Date());
	}
	
	public static Date fromISO8601UTC(String dateStr, String dateFormat) {
		TimeZone tz = TimeZone.getTimeZone("IST");
		SimpleDateFormat df = new SimpleDateFormat(IST_DATE_FORMAT);
		df.setTimeZone(tz);

		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	public static List<InstrumentModel> convertInstrumentModel(List<Instrument> instruments) {
		List<InstrumentModel> instrumentModelList = new ArrayList<InstrumentModel>();

		if (instruments != null && instruments.size() > 0) {
			for (int i = 0; i < instruments.size(); i++) {
				Instrument instrument = instruments.get(i);
				InstrumentModel model = new InstrumentModel();
				model.setId(i);
				model.setExchange(instrument.getExchange());
				model.setExchange_token(instrument.getExchange_token());
				if (instrument.getExpiry() != null) {
					model.setExpiry(toISO8601UTC(instrument.getExpiry(), MONGODB_DATE_FORMAT));
				}
				model.setInstrument_type(instrument.getInstrument_type());
				model.setInstrumentToken(instrument.getInstrument_token());
				model.setLast_price(instrument.getLast_price());
				model.setLot_size(instrument.getLot_size());
				model.setName(instrument.getName());
				model.setSegment(instrument.getSegment());
				model.setStrike(instrument.getStrike());
				model.setTick_size(instrument.getTick_size());
				model.setTradingSymbol(instrument.getTradingsymbol());
				model.setSegment(instrument.getSegment());
				instrumentModelList.add(model);
			}
		}
		return instrumentModelList;
	}
	public static Date fromISO8601UTC(String dateStr) {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
		DateFormat df = new SimpleDateFormat(MONGODB_DATE_FORMAT);
		df.setTimeZone(timeZone);

		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	public com.equitybot.trade.db.mongodb.tick.domain.Tick convertTickModel(com.zerodhatech.models.Tick tick) {
		com.equitybot.trade.db.mongodb.tick.domain.Tick tickModel=null;
		if(tick!=null) {
			tickModel = new com.equitybot.trade.db.mongodb.tick.domain.Tick();
			tickModel.setAverageTradePrice(tick.getAverageTradePrice());
			tickModel.setChange(tick.getChange());
			tickModel.setClosePrice(tick.getClosePrice());
			tickModel.setHighPrice(tick.getHighPrice());
			tickModel.setId(UUID.randomUUID().toString());
			tickModel.setInstrumentToken(tick.getInstrumentToken());
			tickModel.setLastTradedPrice(tick.getLastTradedPrice());
			tickModel.setLastTradedQuantity(tick.getLastTradedQuantity());
			tickModel.setLastTradedTime(tick.getLastTradedTime());
			tickModel.setLowPrice(tick.getLowPrice());
			tickModel.setMode(tick.getMode());
			tickModel.setOi(tick.getOi());
			tickModel.setOiDayHigh(tick.getOpenInterestDayHigh());
			tickModel.setOiDayLow(tick.getOpenInterestDayLow());
			tickModel.setOpenPrice(tick.getOpenPrice());
			tickModel.setTickTimestamp(tick.getTickTimestamp());
			tickModel.setTotalBuyQuantity(tick.getTotalBuyQuantity());
			tickModel.setTotalSellQuantity(tick.getTotalSellQuantity());
			tickModel.setTradable(tick.isTradable());
			tickModel.setVolumeTradedToday(tick.getVolumeTradedToday());
			Map<String, ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>> depth=new HashMap<String, ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>>();
			if(tick.getMarketDepth()!=null && tick.getMarketDepth().size()>0) {
				Map<String, ArrayList<Depth>> marketDepth=tick.getMarketDepth();
				marketDepth.forEach((k,v)->{
					 List<Depth> depthList=v;
					 ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth> mongoDepthList=new ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>();
					 depthList.forEach(item->{
						 com.equitybot.trade.db.mongodb.tick.domain.Depth depthObj=new com.equitybot.trade.db.mongodb.tick.domain.Depth();
						 depthObj.setId(UUID.randomUUID().toString());	
						 depthObj.setOrders(item.getOrders());
							depthObj.setPrice(item.getPrice());
							depthObj.setQuantity(item.getQuantity());
							mongoDepthList.add(depthObj);
						});
					 depth.put(k, mongoDepthList);
				 });
				 tickModel.setDepth(depth);
			}
			
		}
	
		return tickModel;
	}
	
	public static List<Tick> loadHistoricalDataSeries(HistoricalData historicalData, long instrumentToken) {
		List<Tick> tickList=new ArrayList<Tick>();
		if (historicalData != null && historicalData.dataArrayList!=null && historicalData.dataArrayList.size() > 0) {
				List<HistoricalData> listArray=historicalData.dataArrayList;
				for (HistoricalData historicalData2 : listArray) {
					System.out.println("Historical Data Timestamp: "+historicalData2.timeStamp);
					Date from = fromISO8601UTC(historicalData2.timeStamp);
					//System.out.println(" From: "+from);
					Tick tick=new Tick();
					tick.setTickTimestamp(from);
					tick.setClosePrice(historicalData2.close);
					tick.setOpenPrice(historicalData2.open);
					tick.setHighPrice(historicalData2.high);
					tick.setLowPrice(historicalData2.low);
					tick.setLastTradedPrice(historicalData2.close);
					tick.setLastTradedQuantity(historicalData2.volume);
					tick.setInstrumentToken(instrumentToken);
				    Map<String, ArrayList<Depth>> depth=new HashMap<String, ArrayList<Depth>>();
				    ArrayList<Depth> depthList1=new ArrayList<Depth>();
				    
				    Depth obj1=new Depth();
				    obj1.setOrders(40);
				    obj1.setPrice(212.12);
				    obj1.setQuantity(100);
				    depthList1.add(obj1);
				    
				    Depth obj2=new Depth();
				    obj2.setOrders(40);
				    obj2.setPrice(218.12);
				    obj2.setQuantity(200);
				    depthList1.add(obj2);
				    
				    Depth obj3=new Depth();
				    obj3.setOrders(40);
				    obj3.setPrice(214.12);
				    obj3.setQuantity(300);
				    depthList1.add(obj3);
				    
				    Depth obj4=new Depth();
				    obj4.setOrders(40);
				    obj4.setPrice(290.12);
				    obj4.setQuantity(400);
				    
				    depthList1.add(obj4);
				    depth.put("Buy", depthList1);
				    
				    List<Depth> depthList2=new ArrayList<Depth>();
					
				    Depth obj5=new Depth();
				    obj5.setOrders(30);
				    obj5.setPrice(3432.12);
				    obj5.setQuantity(4400);
				    depthList2.add(obj5);
				    
				    Depth obj6=new Depth();
				    obj6.setOrders(40);
				    obj6.setPrice(34.12);
				    obj6.setQuantity(200);
				    depthList2.add(obj6);
				    
				    Depth obj7=new Depth();
				    obj7.setOrders(50);
				    obj7.setPrice(324.12);
				    obj7.setQuantity(4500);
				    depthList2.add(obj7);
				    
				    Depth obj8=new Depth();
				    obj8.setOrders(60);
				    obj8.setPrice(53.12);
				    obj8.setQuantity(453);
				    
				    depthList2.add(obj8);
				    depth.put("Sell", depthList1);
				    tick.setMarketDepth(depth);
				    tickList.add(tick);
					
				}
		}
		return tickList;
	}
	
	
	public static final String KITE_TICK_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final DateFormat dateFormat;
    private static Map<String, ArrayList<Depth>> depth;

    static {
        dateFormat = new SimpleDateFormat(KITE_TICK_TIMESTAMP, new Locale("en", "IN"));
        depth = getDepth();
    }
    
    public static List<Tick> getTickList(HistoricalData historicalDatas, long instrumentToken) throws ParseException {
		List<Tick> tickList=new ArrayList<Tick>();
		if (historicalDatas != null && historicalDatas.dataArrayList!=null && historicalDatas.dataArrayList.size() > 0) {
				for (HistoricalData historicalData : historicalDatas.dataArrayList) {
					tickList.add(mapInTick(historicalData, instrumentToken));
				}
		}
		return tickList;
	}

    
	
	public static Tick mapInTick(HistoricalData historicalData, Long instrument) throws ParseException {
        Date from = getDate(historicalData.timeStamp);
        Tick tick = new Tick();
        tick.setTickTimestamp(from);
        tick.setClosePrice(historicalData.close);
        tick.setOpenPrice(historicalData.open);
        tick.setHighPrice(historicalData.high);
        tick.setLowPrice(historicalData.low);
        tick.setLastTradedPrice(historicalData.close);
        tick.setLastTradedQuantity(historicalData.volume);
        tick.setInstrumentToken(instrument);
        tick.setMarketDepth(depth);
        return tick;
    }
	
	private static Date getDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
	
	private static Map<String, ArrayList<Depth>> getDepth() {
        Map<String, ArrayList<Depth>> depths = new HashMap<>();
        ArrayList<Depth> buys = new ArrayList<>();
        ArrayList<Depth> sell = new ArrayList<>();
        Depth depth = new Depth();
        depth.setOrders(40);
        depth.setPrice(212.12);
        depth.setQuantity(100);
        buys.add(depth);
        depth = new Depth();
        depth.setOrders(40);
        depth.setPrice(218.12);
        depth.setQuantity(200);
        buys.add(depth);
        depth = new Depth();
        depth.setOrders(40);
        depth.setPrice(214.12);
        depth.setQuantity(300);
        buys.add(depth);
        depth = new Depth();
        depth.setOrders(40);
        depth.setPrice(290.12);
        depth.setQuantity(400);
        buys.add(depth);
        depths.put("buy", buys);
        depth = new Depth();
        depth.setOrders(30);
        depth.setPrice(3432.12);
        depth.setQuantity(4400);
        sell.add(depth);
        depth = new Depth();
        depth.setOrders(40);
        depth.setPrice(34.12);
        depth.setQuantity(200);
        sell.add(depth);
        depth = new Depth();
        depth.setOrders(50);
        depth.setPrice(324.12);
        depth.setQuantity(4500);
        sell.add(depth);
        depth = new Depth();
        depth.setOrders(60);
        depth.setPrice(53.12);
        depth.setQuantity(453);
        sell.add(depth);
        depths.put("sell", sell);
        return depths;

    }
	
	
	public static ZonedDateTime convertKiteTickTimestampFormat(String kiteTimestamp) throws ParseException {
		return ZonedDateTime.ofInstant(new SimpleDateFormat(KITE_TICK_TIMESTAMP_FORMAT, Locale.US).parse(kiteTimestamp).toInstant(), ZoneId.systemDefault());
	}
	
}

