package uo.asw.incidashboard.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Propiedad;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.incidashboard.repositories.IncidenciaRepository;

@Service
public class IncidenciasService {

	@Autowired
	private IncidenciaRepository incidenciaRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PropiedadesService pService;

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

	public List<Incidencia> getIncidenciasByOperario(Usuario operario) {
		return incidenciaRepository.findByOperario(operario);
	}

	public void deleteAll() {
		incidenciaRepository.deleteAll();
	}

	public void modifyProperties(Propiedad property) {
		List<Incidencia> incis = incidenciaRepository.findAll();
		for (Incidencia i : incis) {
			modifyPropInci(i, property);
			incidenciaRepository.save(i);
		}
	}

	private void modifyPropInci(Incidencia i, Propiedad property) {
		for (Propiedad p : i.getPropiedades()) {
			if (p.getPropiedad() == property.getPropiedad()) {

				pService.addPropiedad(p);
			}
		}
	}

	public Double[] getDataCircle() {
		Double[] ret = new Double[EstadoTipos.values().length];
		int aux = 0;
		List<Incidencia> inci = incidenciaRepository.findAll();
		for (EstadoTipos eT : EstadoTipos.values()) {
			ret[aux] = calculatePercentage(eT, inci);
			aux++;
		}
		return ret;
	}

	private Double calculatePercentage(EstadoTipos state, List<Incidencia> inci) {
		List<Double> values = new ArrayList<Double>();
		Double aux = 0.0;
		for (Incidencia i : inci) {
			if (i.getEstado() == state)
				values.add(aux++);
		}
		return (values.size() * 100.0) / inci.size();
	}

	private Map<String, Integer> getIncidencias10dias(List<Incidencia> incidencias) {
		Map<String, Integer> datos = new HashMap<>();
		Date hoy = new Date();

		Calendar c1 = Calendar.getInstance();
		c1.setTime(hoy);

		datos.put((new SimpleDateFormat("dd/MM/yyyy")).format(hoy), 0);
		for (int i = 0; i < 9; i++) {
			c1.add(Calendar.DAY_OF_MONTH, -1);
			Date hace10dias = c1.getTime();
			datos.put((new SimpleDateFormat("dd/MM/yyyy")).format(hace10dias), 0);
			c1.setTime(hace10dias);
		}

		for (Incidencia inci : incidencias) {
			String f = (new SimpleDateFormat("dd/MM/yyyy")).format(inci.getFechaEntrada());
			if (datos.get(f) != null) {
				datos.put(f, (datos.get(f) + 1));
			}
		}

		return datos;
	}

	public String[] getDays(List<Incidencia> incidencias) {
		Map<String, Integer> datos = getIncidencias10dias(incidencias);
		String[] result = new String[datos.size()];
		
		int i=0;
		for (Entry<String, Integer> entry : datos.entrySet()) {
			result[i] = entry.getKey();
			i++;
		}
		Arrays.sort(result);
		return result;
	}

	public int[] getNum(List<Incidencia> incidencias) {
		Map<String, Integer> datos = getIncidencias10dias(incidencias);
		String[] aux = getDays(incidencias);

		int[] result = new int[aux.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = datos.get(aux[i]);
		}

		return result;
	}
	
	public List<Incidencia> getAllIncidencias(){
		return incidenciaRepository.findAll();
	}
}
