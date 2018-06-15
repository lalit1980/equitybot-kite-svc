package com.equitybot.trade.db.mongodb.instrument.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;;

@Document(collection = "Instruments")
public class InstrumentModel{
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1800963524269704664L;
	@Id
	private long id;
	private long instrumentToken;
	private long exchange_token;
	private String tradingSymbol;
	private String name;
	private double last_price;
	private double tick_size;
	private String instrument_type;
	private String segment;
	private String exchange;
	private String  strike;
	private int lot_size;
	private String expiry;

	
	public long getInstrumentToken() {
		return instrumentToken;
	}


	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}


	public long getExchange_token() {
		return exchange_token;
	}


	public void setExchange_token(long exchange_token) {
		this.exchange_token = exchange_token;
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public double getLast_price() {
		return last_price;
	}


	public void setLast_price(double last_price) {
		this.last_price = last_price;
	}

	
	public double getTick_size() {
		return tick_size;
	}


	public void setTick_size(double tick_size) {
		this.tick_size = tick_size;
	}

	
	public String getInstrument_type() {
		return instrument_type;
	}


	public void setInstrument_type(String instrument_type) {
		this.instrument_type = instrument_type;
	}

	
	public String getSegment() {
		return segment;
	}


	public void setSegment(String segment) {
		this.segment = segment;
	}

	
	public String getExchange() {
		return exchange;
	}


	public void setExchange(String exchange) {
		this.exchange = exchange;
	}


	public String getStrike() {
		return strike;
	}


	public void setStrike(String strike) {
		this.strike = strike;
	}


	public int getLot_size() {
		return lot_size;
	}


	public void setLot_size(int lot_size) {
		this.lot_size = lot_size;
	}


	public String getExpiry() {
		return expiry;
	}


	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}


	public String getTradingSymbol() {
		return tradingSymbol;
	}


	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "InstrumentModel [id=" + id + ", instrumentToken=" + instrumentToken + ", exchange_token="
				+ exchange_token + ", tradingSymbol=" + tradingSymbol + ", name=" + name + ", last_price=" + last_price
				+ ", tick_size=" + tick_size + ", instrument_type=" + instrument_type + ", segment=" + segment
				+ ", exchange=" + exchange + ", strike=" + strike + ", lot_size=" + lot_size + ", expiry=" + expiry
				+ "]";
	}


}
