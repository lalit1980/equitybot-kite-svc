package com.equitybot.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equitybot.kite.KiteOperation;
import com.equitybot.manager.cache.ManagerCache;

@Service
public class PlaceOrderService {
	
	@Autowired
	private ManagerCache managerCache;
	@Autowired
	private KiteOperation kiteOperation;
	
	
	public void serve(Long instrument ,String action){
		
    }

}
