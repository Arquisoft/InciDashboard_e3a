package inciDashboard.uo.asw.kafkaConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import inciDashboard.uo.asw.dbManagement.model.Incidencia;
import inciDashboard.uo.asw.mvc.services.IncidenciasService;

@Service
public class KafkaConsumer {
	
	@Autowired
	private IncidenciasService inci;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Se encarga de procesar la incidencia recibida desde el productor
	 * Kafka, ademas, se guarda en un login
	 * @param incidencia que se recibe
	 */
	@KafkaListener(topics = "${kafka.topic}")
	public void processMessage(Incidencia incidencia) {
		inci.addIncidenciaDesdeKafka(incidencia);
		log.info("Incidencia " + incidencia.getNombreIncidencia() + " recibida desde Apache Kafka");
	}
}
