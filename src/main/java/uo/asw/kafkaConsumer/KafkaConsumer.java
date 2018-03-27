package uo.asw.kafkaConsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;


@Service
public class KafkaConsumer {
	
	@KafkaListener(topics="${kafka.topic}")
    public void processMessage(Incidencia incidencia) {
		System.out.println("Recibida = " + incidencia.toStringJson()); //quitar y enviar a un log
    }
}
