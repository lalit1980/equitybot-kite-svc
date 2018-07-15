package com.equitybot.trade.messaging.kafka.listener;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.google.gson.Gson;

public class OrderReceiverListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.producer.topic-kite-tradeorder}")
	private String timeSeriesProducerTopic;

	@KafkaListener(id = "id0", topicPartitions = {
			@TopicPartition(topic = "topic-kite-tradeorder", partitions = { "0" }) })
	public void listenPartition0(ConsumerRecord<?, ?> record) throws IOException {
		Gson gson = new Gson();
		OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
	}

	@KafkaListener(id = "id1", topicPartitions = {
			@TopicPartition(topic = "topic-kite-tradeorder", partitions = { "1" }) })
	public void listenPartition1(ConsumerRecord<?, ?> record) throws IOException {
		Gson gson = new Gson();
		OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
	}

	@KafkaListener(id = "id2", topicPartitions = {
			@TopicPartition(topic = "topic-kite-tradeorder", partitions = { "2" }) })
	public void listenPartition2(ConsumerRecord<?, ?> record) throws IOException {
		Gson gson = new Gson();
		OrderRequestDTO tradeBO= gson.fromJson(record.value().toString(), OrderRequestDTO.class);
	}

	
}
