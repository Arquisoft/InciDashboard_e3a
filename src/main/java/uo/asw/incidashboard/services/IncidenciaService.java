package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.model.Incidencia;
import uo.asw.incidashboard.repositories.IncidenciaRepository;
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

	public Page<Incidencia> getIncidencias(Pageable pageable, Long id_agente) {
		return incidenciaRepository.findIncidenciasByIdAgent(pageable, id_agente);
	}

	public void addIncidencia(Incidencia incidencia) {

		incidenciaRepository.save(incidencia);
	}

	public Page<Incidencia> getUserIncidencias(Pageable pageable, Long idUser) {
		return incidenciaRepository.findIncidenciasByIdUser(pageable, idUser);
	}

	public void deleteIncidencia(Long id) {
		incidenciaRepository.deleteById(id);
	}

	public Incidencia getIncidenciaByName(String nombre) {
		return incidenciaRepository.findIncidenciByName(nombre);
	}
}
