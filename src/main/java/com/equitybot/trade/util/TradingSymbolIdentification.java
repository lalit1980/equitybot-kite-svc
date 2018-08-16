package com.equitybot.trade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TradingSymbolIdentification {
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	public long getIndexValue(long currentIndex) {
		return (long) Math.floor(currentIndex/100)*100;
	}
	
	public String getDateNextThursday() {
		Calendar now=Calendar.getInstance();
		String dateMonth=null;
		LocalDate dateOnThursday=LocalDate.now(ZoneId.of("Asia/Calcutta")).with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
		int weekNumber=now.get(Calendar.WEEK_OF_MONTH);
		if(weekNumber!=5) {
			dateMonth=dateOnThursday.format(DateTimeFormatter.ofPattern("ddMMYY")).toUpperCase().trim();
		}else {
			dateMonth=dateOnThursday.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase().trim();
		}
		return dateMonth;
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		String time="2018-08-16 11:01:00";
		Date date = formatter.parse(time);
       System.out.println(date);

       
		
	}

}
