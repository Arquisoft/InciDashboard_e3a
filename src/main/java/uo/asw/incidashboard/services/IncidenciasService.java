package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.incidashboard.repositories.IncidenciaRepository;
import uo.asw.incidashboard.repositories.UsuarioRepository;

@Service
public class IncidenciasService {
	@Autowired
	private IncidenciaRepository incidenciaRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostConstruct
	public void init() {
		asignacionIncidencias();
	}

	public void asignacionIncidencias() {
		List<Incidencia> incidencias = getIncidencias();
		for (int i = 0; i < incidencias.size(); i++) {
			if (incidencias.get(i).getEstado().equals(EstadoTipos.ABIERTA)
					&& incidencias.get(i).getOperario() == null) {
				Usuario user = usuarioService.getUsuarioConMenosIncis();
				getIncidencias().get(i).setOperario(user);
				getIncidencias().get(i).setEstado(EstadoTipos.EN_PROCESO);
				incidenciaRepository.save(getIncidencias().get(i));

			}
		}
	}

	public List<Incidencia> getIncidencias() {
		List<Incidencia> incidencias = new ArrayList<Incidencia>();
		incidenciaRepository.findAll().forEach(incidencias::add);
		return incidencias;
	}

	// public Page<Incidencia> getIncidencias(Pageable pageable, ObjectId
	// id_agente) {
	// return incidenciaRepository.findIncidenciasByIdAgent(pageable,
	// id_agente);
	// }

	public Page<Incidencia> getIncis(Pageable pageable) {
		return incidenciaRepository.findAll(pageable);
	}

	public void addIncidencia(Incidencia incidencia) {

		incidenciaRepository.save(incidencia);
	}

	public Page<Incidencia> getUserIncidencias(Pageable pageable, Usuario u) {
		return incidenciaRepository.findByOperario(u, pageable);
	}

	public void deleteIncidencia(ObjectId id) {
		incidenciaRepository.delete(id);
	}

	public Incidencia getIncidenciaByName(String nombre) {
		return incidenciaRepository.findByNombreIncidencia(nombre);
	}
	
	public List<Incidencia> getIncidenciasByOperario(Usuario operario){
		return incidenciaRepository.findByOperario(operario);
	}
}
