package com.equitybot.trade.order.model;

import java.util.List;

import org.ta4j.core.Bar;

public class AlgorithmModel {
	
	private List<Bar> buyBar;
	private List<Bar> sellBar;
	
	public List<Bar> getBuyBar() {
		return buyBar;
	}
	public void setBuyBar(List<Bar> buyBar) {
		this.buyBar = buyBar;
	}
	
	public List<Bar> getSellBar() {
		return sellBar;
	}
	public void setSellBar(List<Bar> sellBar) {
		this.sellBar = sellBar;
	}

}
