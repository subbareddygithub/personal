package com.scb.gb.sci.publisher.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaConfiguration {

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
	public Map<String, Object> producerConfigs() {

		final Map<String, Object> properties = new HashMap<>();

		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		properties.put("max.request.size", "10000000");

		try {
			properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation.getFile().getPath());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
		properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);

		return properties;

	}
	
	@Bean
	public ProducerFactory<String, String> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public Deserializer<String> stringKeyDeserializer(){
		return new StringDeserializer();
	}

}
