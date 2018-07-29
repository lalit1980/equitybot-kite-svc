package com.equitybot.trade.messaging.kafka.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.order.placeorder.IBuyOrder;
import com.equitybot.trade.order.placeorder.ISellOrder;
import com.google.gson.Gson;
import com.zerodhatech.kiteconnect.utils.Constants;

public class OrderReceiverListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Autowired
	private IBuyOrder buyOrderService;
	
	@Autowired
	private ISellOrder sellOrderService;
/*
	@KafkaListener(topicPartitions = {
			@TopicPartition(topic = "topic-kite-tradeorder", partitions = { "0" }) })
	public void listenPartition0(ConsumerRecord<?, ?> record) throws IOException {
		Gson gson = new Gson();
		OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
		placeTradeOrder(tradeBO);
	}
*/
	 @KafkaListener(topicPartitions = {@TopicPartition(topic = "topic-kite-tradeorder", partitions = {"0"})})
	    public void listenPartition0(ConsumerRecord<?, ?> record) throws IOException {
		 Gson gson = new Gson();
			OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
			placeTradeOrder(tradeBO);
	    }

	    @KafkaListener(topicPartitions = {@TopicPartition(topic = "topic-kite-tradeorder", partitions = {"1"})})
	    public void listenPartition1(ConsumerRecord<?, ?> record) throws IOException {
	    	Gson gson = new Gson();
			OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
			placeTradeOrder(tradeBO);
	    }

	    @KafkaListener(topicPartitions = {@TopicPartition(topic = "topic-kite-tradeorder", partitions = {"2"})})
	    public void listenPartition2(ConsumerRecord<?, ?> record) throws IOException {
	    	Gson gson = new Gson();
			OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
			placeTradeOrder(tradeBO);
	    }
	
	public void placeTradeOrder(OrderRequestDTO tradeBO) {
		logger.info("Received Trade Order: "+tradeBO.toString() );
		if(tradeBO.getTransactionType().equalsIgnoreCase(Constants.TRANSACTION_TYPE_BUY)) {
			buyOrderService.buyOrder(tradeBO);
		}else if(tradeBO.getTransactionType().equalsIgnoreCase(Constants.TRANSACTION_TYPE_SELL)) {
			sellOrderService.sellOrder(tradeBO);
		}
	}
	
}
