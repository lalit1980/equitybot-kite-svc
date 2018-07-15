package com.equitybot.trade.ws.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.dto.OrderResponseDTO;
import com.equitybot.trade.order.placeorder.IBuyOrder;

@RestController
@RequestMapping("/api")
public class OrderServiceController {

	@Autowired
	private IBuyOrder buyOrderService;

	@PostMapping("/tradeorder/v1.0/")
	public OrderResponseDTO placeOrder(
			@RequestBody OrderRequestDTO orderRequestDTO) {
		return buyOrderService.buyOrder(orderRequestDTO);
	}
	
}
