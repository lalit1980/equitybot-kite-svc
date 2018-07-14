package com.equitybot.trade.order.model;

import java.util.List;
import java.util.Map;

import org.ta4j.core.Bar;

public class AlgorithmModel {
	
	private Map<Long, Bar> buyBarMap;
	private Map<Long, Bar> sellBarMap;
	public Map<Long, Bar> getBuyBarMap() {
		return buyBarMap;
	}
	public void setBuyBarMap(Map<Long, Bar> buyBarMap) {
		this.buyBarMap = buyBarMap;
	}
	public Map<Long, Bar> getSellBarMap() {
		return sellBarMap;
	}
	public void setSellBarMap(Map<Long, Bar> sellBarMap) {
		this.sellBarMap = sellBarMap;
	}
	
	
	

}
