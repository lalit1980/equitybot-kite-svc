package com.equitybot.trade.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.tick.domain.Tick;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;


public class DateFormatUtil {

	public static final String KITE_SERVICE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	public static final String KITE_TICK_TIMESTAMP_FORMAT = "MMM dd, yyyy hh:mm:ss a";

	public static final String MONGODB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String TA4J = "yyyy-MM-dd";
	
	public static String toISO8601UTC(Date date, String dateFormat) {

		Format formatter = new SimpleDateFormat(MONGODB_DATE_FORMAT);
		return formatter.format(date);
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
	
	public static List<Tick> loadHistoricalDataSeries(HistoricalData historicalData, long instrumentToken) {
		List<Tick> tickList=new ArrayList<Tick>();
		if (historicalData != null && historicalData.dataArrayList!=null && historicalData.dataArrayList.size() > 0) {
				List<HistoricalData> listArray=historicalData.dataArrayList;
				for (HistoricalData historicalData2 : listArray) {
					Date from = fromISO8601UTC(historicalData2.timeStamp);
					System.out.println(" From: "+from);
					Tick tick=new Tick();
					tick.setTickTimestamp(from);
					tick.setClosePrice(historicalData2.close);
					tick.setOpenPrice(historicalData2.open);
					tick.setHighPrice(historicalData2.high);
					tick.setLowPrice(historicalData2.low);
					tick.setLastTradedPrice(historicalData2.close);
					tick.setLastTradedQuantity(historicalData2.volume);
					tick.setInstrumentToken(instrumentToken);
					tickList.add(tick);
					
				}
		}
		return tickList;
	}

	public static ZonedDateTime convertKiteTickTimestampFormat(String kiteTimestamp) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(KITE_TICK_TIMESTAMP_FORMAT,Locale.US);
		ZoneId zoneid1 = ZoneId.of("Asia/Kolkata");  
		return ZonedDateTime.ofInstant(sdf.parse(kiteTimestamp).toInstant(), zoneid1);
	}
	
}

