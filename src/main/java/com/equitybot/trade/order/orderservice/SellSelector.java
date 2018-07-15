package com.equitybot.trade.order.orderservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ta4j.core.Bar;

import com.zerodhatech.models.Depth;
import com.zerodhatech.models.Tick;

public class SellSelector {
	
	private List<Bar> sellBar;
	private Tick tick;
	
	public SellSelector(List<Bar> sellBar, Tick tick) {
		super();
		this.sellBar = sellBar;
		this.tick = tick;
	}

	private double getBuyPriceFromDepth(Map<String, ArrayList<Depth>> depthMap, int totalOrderQuantity) {
		List<Depth> depths = depthMap.get("sell");
		int totalAvalableSellQuantity = 0;
		for(Depth depth : depths ) {
			totalAvalableSellQuantity = totalAvalableSellQuantity + depth.getQuantity();
			if(totalOrderQuantity <= totalAvalableSellQuantity) {
				return depth.getPrice();
			}
		}
		return 0.0;
	}

	public List<Bar> getSellBar() {
		return sellBar;
	}

	public Tick getTick() {
		return tick;
	}
	

}
