package com.equitybot.trade.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

public class TradingSymbolIdentification {
	
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

}
