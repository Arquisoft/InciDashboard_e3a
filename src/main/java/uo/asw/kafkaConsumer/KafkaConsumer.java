package uo.asw.kafkaConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.incidashboard.services.IncidenciasService;

@Service
public class KafkaConsumer {
	@Autowired
	private IncidenciasService inci;

	@KafkaListener(topics = "${kafka.topic}")
	public void processMessage(Incidencia incidencia) {
		inci.addIncidencia(incidencia);
//		System.out.println("Recibida = " + incidencia.toStringJson()); // quitar y enviar a un log
	}
}
