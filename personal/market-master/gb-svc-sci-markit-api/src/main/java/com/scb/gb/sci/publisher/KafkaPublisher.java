package com.scb.gb.sci.publisher;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class KafkaPublisher {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${spring.kafka.topic.vdm")
	private String topic;

	public void vdmPublish(final String message) {

		final ListenableFuture<SendResult<String, String>> listEnableFuture = kafkaTemplate.send(topic, message);
		try {
			final SendResult<String, String> sendResult = listEnableFuture.get();
			final RecordMetadata recordMetadata = sendResult.getRecordMetadata();
			System.out.println("Topic Name" + recordMetadata.topic());
			System.out.println("offset " + recordMetadata.offset());
			System.out.println("Partition " + recordMetadata.partition());
			System.out.println("Message sent successfully");
		} catch (final InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
}
