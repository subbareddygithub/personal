package com.scb.gb.sci.publisher.properties;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaBrokerProperties {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.topic.vdm}")
	private String vdmTopic;

	@Value("${spring.kafka.ssl.truststore-password}")
	private String trustStorePassword;

	@Value("${spring.kafka.ssl.truststore-location}")
	private Resource trustStoreLocation;

	@Value("${spring.kafka.properties.security.protocol}")
	private String securityProtocol;
	
	@Bean
	public ConsumerFactory<String, String > consumerFactory(){
		return new DefaultKafkaConsumerFactory<>(consumerProperties());
		
	}
	
	@Bean
	public Map<String, Object> consumerProperties(){
		final Map<String, Object> props = new HashMap();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
		try {
			props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation.getFile().getPath());
		}catch(final IOException e) {
			e.printStackTrace();
		}
		
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
		
		return props;
	}

	@Bean
	public <T> ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(){
		
		final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConcurrency(1);
		factory.setConsumerFactory(consumerFactory());
		return factory;
		
	}
	
	@Bean
	public Deserializer<String> stringKeyDeserializer(){
		return new StringDeserializer();
	}
	
	@Bean
	public KafkaConsumer<String, String> kafkaConsumer(){
		final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties());
		consumer.subscribe(Collections.singleton(vdmTopic));
		
		return consumer;
		
	}
}
