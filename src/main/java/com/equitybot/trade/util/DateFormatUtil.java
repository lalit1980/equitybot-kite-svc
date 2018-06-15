package com.equitybot.trade.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;


public class DateFormatUtil {

	public static final String KITE_SERVICE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
	public static final String KITE_TICK_TIMESTAMP_FORMAT = "MMM dd, yyyy hh:mm:ss a";

	public static final String MONGODB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final String TA4J = "yyyy-MM-dd";
	private static final DateTimeFormatter MONGODB_DATE_FORMATER = DateTimeFormatter.ofPattern(MONGODB_DATE_FORMAT);
	private static final DateTimeFormatter KITE_TICK_TIMESTAMP_FORMATER = DateTimeFormatter.ofPattern(KITE_TICK_TIMESTAMP_FORMAT);

	public static String toISO8601UTC(Date date, String dateFormat) {

		Format formatter = new SimpleDateFormat(MONGODB_DATE_FORMAT);
		return formatter.format(date);
	}

	public static Date fromISO8601UTC(String dateStr, String dateFormat) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat(dateFormat);
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
	
	public static TimeSeries loadHistoricalDataSeries(HistoricalData historicalData, String instrumentTocken) {
		TimeSeries series = new BaseTimeSeries(instrumentTocken + "_bars");
		if (historicalData != null && historicalData.dataArrayList!=null && historicalData.dataArrayList.size() > 0) {
				List<HistoricalData> listArray=historicalData.dataArrayList;
				listArray.forEach(listArr -> {
					//series.addBar(LocalDate.parse(listArr.timeStamp, MONGODB_DATE_FORMATER).atStartOfDay(ZoneId.systemDefault()), listArr.open, listArr.high, listArr.low, listArr.close,listArr.volume);
					 System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
				        System.out.println("Number of bars: " + series.getBarCount());
				        System.out.println(" Bar: \n"
				                + "\tVolume: " + series.getBar(0).getVolume() + "\n"
				                + "\tOpen price: " + series.getBar(0).getOpenPrice()+ "\n"
				                + "\tClose price: " + series.getBar(0).getClosePrice());
				});
				
		}
		return series;
	}

	public static ZonedDateTime convertKiteTickTimestampFormat(String kiteTimestamp) throws ParseException {
		return ZonedDateTime.ofInstant(new SimpleDateFormat(KITE_TICK_TIMESTAMP_FORMAT, Locale.US).parse(kiteTimestamp).toInstant(), ZoneId.systemDefault());
	}
	
}

