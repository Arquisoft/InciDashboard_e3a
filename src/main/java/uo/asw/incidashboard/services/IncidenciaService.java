package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.incidashboard.repositories.IncidenciaRepository;

@Service
public class IncidenciaService {

	@Autowired
	private IncidenciaRepository incidenciaRepository;

	public List<Incidencia> getIncidencias() {
		List<Incidencia> incidencias = new ArrayList<Incidencia>();
		incidenciaRepository.findAll().forEach(incidencias::add);
		return incidencias;
	}

	public Page<Incidencia> getIncidencias(Pageable pageable, String id_agente) {
		return incidenciaRepository.findIncidenciasByIdAgent(pageable, id_agente);
	}

	public Page<Incidencia> getIncis(Pageable pageable) {
		return incidenciaRepository.findIncidencias(pageable);
	}

	public void addIncidencia(Incidencia incidencia) {

		incidenciaRepository.save(incidencia);
	}

	public Page<Incidencia> getUserIncidencias(Pageable pageable, ObjectId objectId) {
		return incidenciaRepository.findIncidenciasByIdUser(pageable, objectId);
	}

	public void deleteIncidencia(ObjectId id) {
		incidenciaRepository.deleteById(id);
	}

	public Incidencia getIncidenciaByName(String nombre) {
		return incidenciaRepository.findIncidenciByName(nombre);
	}
}
