package uo.asw.kafkaConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;

@Service
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
 
	@Bean
	public ConsumerFactory<String, Incidencia> consumerFactory() {
		String usuario = "fsj71lf2";
		String password = "1XUrUi1Zy9lopTAwx5q-Jho3UfkWSFZN";
		String jaasConfig = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + usuario + "\" password=\"" + password + "\";";
		String btServer = "ark-01.srvs.cloudkafka.com:9094,ark-02.srvs.cloudkafka.com:9094,ark-03.srvs.cloudkafka.com:9094";
		Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, btServer);
		props.put("group.id", usuario + "-consumer");
		props.put("security.protocol", "SASL_SSL");
		props.put("sasl.mechanism", "SCRAM-SHA-256");
		props.put("sasl.jaas.config", jaasConfig);
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