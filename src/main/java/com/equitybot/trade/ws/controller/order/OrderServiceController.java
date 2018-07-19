package com.equitybot.trade.ws.controller.order;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.db.mongodb.order.domain.TradeRequestDTO;
import com.equitybot.trade.db.mongodb.order.repository.OrderResponseRepository;
import com.equitybot.trade.order.placeorder.IBuyOrder;
import com.equitybot.trade.ws.service.kite.KiteConnectService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;

@RestController
@RequestMapping("/api")
public class OrderServiceController {

	@Autowired
	private IBuyOrder buyOrderService;

	@Autowired
	OrderResponseRepository orderResponseRepository;

	@Autowired
	KiteConnectService kiteConnectService;

	@PostMapping("/tradeorder/v1.0/")
	public OrderResponse placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
		return buyOrderService.buyOrder(orderRequestDTO);
	}

	@GetMapping("/tradeorder/v1.0/orderId/{orderId}")
	public OrderResponse findByOrderId(@PathVariable("orderId") String orderId) {
		return orderResponseRepository.findByOrderId(orderId);
	}

	@GetMapping("/tradeorder/v1.0/userId/{userId}")
	public List<OrderResponse> findAllByUserId(@PathVariable("userId") String userId) {
		return orderResponseRepository.findAllByUserId(userId);
	}

	@GetMapping("/tradeorder/v1.0/tradingSymbol/{tradingSymbol}")
	public List<OrderResponse> findAllByTradingSymbol(@PathVariable("tradingSymbol") String tradingSymbol) {
		return orderResponseRepository.findAllByTradingSymbol(tradingSymbol);
	}

	@GetMapping("/tradeorder/v1.0/orderStatus/{orderStatus}")
	public List<OrderResponse> findAllByOrderStatus(@PathVariable("orderStatus") String orderStatus) {
		return orderResponseRepository.findAllByOrderStatus(orderStatus);
	}

	@GetMapping("/tradeorder/v1.0/transactionType/{transactionType}")
	public List<OrderResponse> findAllByTransactionType(@PathVariable("orderStatus") String orderStatus) {
		return orderResponseRepository.findAllByTransactionType(orderStatus);
	}

	@GetMapping("/tradeorder/v1.0/instrumentToken/{instrumentToken}")
	public List<OrderResponse> findAllByInstrumentToken(@PathVariable("instrumentToken") String instrumentToken) {
		return orderResponseRepository.findAllByInstrumentToken(instrumentToken);
	}

	@PostMapping({ "/tradeorder/v1.0" })
	public OrderResponse add(@RequestBody OrderResponse orderResponse) {
		return orderResponseRepository.save(orderResponse);
	}

	@GetMapping("/tradeorder/v1.0/userId/{userId}/requestToken/{requestToken}")
	public List<Order> findAllOrderFromZerodha(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken) {
		try {

			List<Order> orderList = kiteConnectService.getOrders(userId, requestToken);
			OrderRequestDTO tradeRequest = new OrderRequestDTO();
			tradeRequest.setUserId(userId);
			tradeRequest.setRequestToken(requestToken);
			orderResponseRepository.deleteAll();
			if (orderList != null && orderList.size() > 0) {
				for (Order order : orderList) {
					orderResponseRepository.save(kiteConnectService.convertOrderResponse(order, tradeRequest));
				}
			}
			return orderList;
		} catch (IOException | KiteException e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("/tradeorder/v1.0/userId/{userId}/requestToken/{requestToken}/orderId/{orderId}")
	public List<Order> findOrderFromZerodha(@PathVariable("userId") String userId,
			@PathVariable("requestToken") String requestToken, @PathVariable("orderId") String orderId) {
		try {
			List<Order> orderList = kiteConnectService.getOrder(userId, requestToken, orderId);
			return orderList;
		} catch (IOException | KiteException e) {
			e.printStackTrace();
			return null;
		}

	}
}
