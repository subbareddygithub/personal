package com.scb.gb.sci.subscriber;

import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scb.gb.sci.publisher.properties.KafkaBrokerProperties;

@Service
public class KafkaSubscriber {
	
	@Autowired
	private KafkaBrokerProperties kafkaConfiguration;
	
	
	public void vdmConsume() throws Exception {
		
		KafkaConsumer<String, String> consumer = kafkaConfiguration.kafkaConsumer();
		
		boolean flag = true;
		
		while(true) {
			@SuppressWarnings("deprecation")
			ConsumerRecords<String, String> records = consumer.poll(100);
			if(flag) {
				Set<TopicPartition> assignments = consumer.assignment();
				assignments.forEach(topicPartition -> consumer.seek(topicPartition, 0));
				
				flag = false;
			}
				
			 for(ConsumerRecord<String, String> record : records)
				 System.out.printf("offset = %d, value = %s%n", record.offset(), record.value());
			}
		}
	}


