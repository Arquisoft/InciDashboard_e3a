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
public class IncidenciasService {

	@Autowired
	private IncidenciaRepository incidenciaRepository;

	@Autowired
	public List<Incidencia> getIncidencias() {
		List<Incidencia> incidencias = new ArrayList<Incidencia>();
		incidenciaRepository.findAll().forEach(incidencias::add);
		return incidencias;
	}

	@Autowired
	public Page<Incidencia> getIncidencias(Pageable pageable, String id_agente) {
		return incidenciaRepository.findIncidenciaByIdAgent(id_agente, pageable);
	}

	@Autowired
	public Page<Incidencia> getIncis(Pageable pageable) {
		return incidenciaRepository.findAll(pageable);
	}

	@Autowired
	public void addIncidencia(Incidencia incidencia) {

		incidenciaRepository.save(incidencia);
	}

	@Autowired
	public Page<Incidencia> getUserIncidencias(Pageable pageable, ObjectId objectId) {
		return incidenciaRepository.findIncidenciaByIdUser(objectId, pageable);
	}

	@Autowired
	public void deleteIncidencia(ObjectId id) {
		incidenciaRepository.deleteById(id);
	}

	@Autowired
	public Incidencia getIncidenciaByName(String nombre) {
		return incidenciaRepository.findIncidenciabyNombreIncidencia(nombre);
	}
}
