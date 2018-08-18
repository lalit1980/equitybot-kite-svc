package com.equitybot.trade.util;

public class TradingSymbolIdentification {
	public long getIndexValue(long currentIndex) {
		return (long) Math.floor(currentIndex/100)*100;
	}
	public static void main(String[] args){
		
		
	}

}
