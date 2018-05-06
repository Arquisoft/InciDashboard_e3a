package uo.asw.kafkaConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;



@Configuration
@EnableKafka
public class KafkaConsumerConfig {
 

	
	@Value("${spring.kafka.producer.bootstrap-servers}")
	String server;
	@Value("${karafka.username}")
	String username;
	@Value("${karafka.password}")
	String password;
	@Value("${kafka.protocol}")
	String protocol;

	@Bean
	public ConsumerFactory<String, Incidencia> consumerFactory() {
		String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
		String jaasCfg = String.format(jaasTemplate, username, password);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "es.uniovi");
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol);
		props.put("sasl.mechanism", "SCRAM-SHA-256");
		props.put("sasl.jaas.config", jaasCfg);
		return new DefaultKafkaConsumerFactory<>(props,
	    	      new StringDeserializer(), 
	    	      new JsonDeserializer<>(Incidencia.class));
	}
	
	
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Incidencia> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, Incidencia> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    return factory;
	}
}