package uo.asw.incidashboard.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import uo.asw.dbManagement.model.ValorLimite;
import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.dbManagement.tipos.PerfilTipos;
import uo.asw.incidashboard.repositories.IncidenciaRepository;

@Service
public class IncidenciasService {

	private List<Incidencia> notificacionesKafka = new ArrayList<Incidencia>();
	@Autowired
	private IncidenciaRepository incidenciaRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PropiedadesService pService;
	@Autowired
	private ValorLimiteService vLimService;
	
	private Usuario userConnected;
	
	private List<Incidencia> lInciKafka = new ArrayList<Incidencia>();

	public void addIncidenciaDesdeKafka(Incidencia incidencia) {
		incidencia = asignacionIncidencias(incidencia);

		// Para notificaciones a operarios

		if (lInciKafka.size() < 8)
			lInciKafka.add(0, incidencia);
		else {
			lInciKafka.remove(lInciKafka.size() - 1);
			lInciKafka.add(0, incidencia);
		}

		if (userConnected.getPerfil() == PerfilTipos.OPERARIO) {
			comprobarIncidencia(incidencia);
		}
	}
	
	public void setUserConnected(String email) {
		userConnected = usuarioService.getUsuarioByMail(email);
	}

	private void comprobarIncidencia(Incidencia incidencia) {
		if (!notificacionesKafka.contains(incidencia)) {
			if (incidencia.getOperario().getIdentificador().equals(userConnected.getIdentificador())) {
				notificacionesKafka.add(incidencia);
			}
		}
	}

	public void inicializarListaNotificaciones() {
		notificacionesKafka = new ArrayList<Incidencia>();
	}

	public void eliminarIncidencia(String id) {
		Incidencia inciF = null;
		for (Incidencia inci : incidenciaRepository.findAll()) {
			String param = id.split("'")[1];
			String casa = inci.getId().toString();
			if (param.equalsIgnoreCase(casa) == true) {
				inciF = inci;
				break;
			}
		}
		notificacionesKafka.remove(inciF);
	}

	public List<Incidencia> getLInciKafka() {
		return setCritics(lInciKafka);
	}

	public Incidencia asignacionIncidencias(Incidencia incidencia) {
		Usuario user = usuarioService.getUsuarioConMenosIncis();
		incidencia.setOperario(user);
		incidenciaRepository.save(incidencia);
		return incidencia;
	}

	public Page<Incidencia> getIncis(Pageable pageable) {
		return incidenciaRepository.findAll(pageable);
	}

	public void addIncidencia(Incidencia incidencia) {
		incidenciaRepository.save(incidencia);
	}

	public Page<Incidencia> getUserIncidencias(Pageable pageable, Usuario u) {
		Page<Incidencia> incis = incidenciaRepository.findByOperario(u, pageable);
		for (Incidencia inci : incis.getContent()) {
			inci.setId_string(inci.getId().toHexString());
		}
		return incis;
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

		int i = 0;
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

	public List<Incidencia> getAllIncidencias() {
		return incidenciaRepository.findAll();
	}

	public void changeState(String id, String nuevoEstado) {
		Incidencia inciF = null;
		for (Incidencia inci : incidenciaRepository.findAll()) {
			String param = id.split("'")[1];
			String casa = inci.getId().toString();
			if (param.equalsIgnoreCase(casa) == true) {
				inciF = inci;
				break;
			}
		}

		if (nuevoEstado.toCharArray()[0] == 'E')
			inciF.setEstado(EstadoTipos.EN_PROCESO);

		else if (nuevoEstado.toCharArray()[1] == 'N')
			inciF.setEstado(EstadoTipos.ANULADA);

		else if (nuevoEstado.toCharArray()[0] == 'C')
			inciF.setEstado(EstadoTipos.CERRADA);

		else if (nuevoEstado.toCharArray()[1] == 'B')
			inciF.setEstado(EstadoTipos.ABIERTA);

		else
			return;

		incidenciaRepository.save(inciF);
	}

	public boolean incidenciaConValorLimite(Incidencia i) {
		for (Propiedad p : i.getPropiedades()) {
			ValorLimite vL = vLimService.findByPropiedad(p.getPropiedad().toString());
			if (vL != null)
				return vL.isMaxCritico() || vL.isMinCritico();
		}
		return false;
	}

	public List<Incidencia> setCritics(List<Incidencia> incis) {
		List<Incidencia> aux = new ArrayList<Incidencia>(incis);
		for (Incidencia i : aux) {
			if (incidenciaConValorLimite(i))
				i.setCritics(true);
			else
				i.setCritics(false);
		}
		return aux;
	}

	public List<Incidencia> getUrlImgs(List<Incidencia> incidencias) {
		List<Incidencia> urls = new ArrayList<Incidencia>();
		String urlBase = "http://localhost:8090/incidencia/";
		for (Incidencia i : incidencias) {
			if (i.getImageURL() != null) { // OJO: no guardo en BD.
				String nombreImg = i.getImageURL().substring(10, i.getImageURL().length() - 4); // Desde el principio
																								// del nombre, hasta que
																								// empieza el jpg
				i.setImageURL(urlBase + nombreImg);
				System.out.println(i.getImageURL());
				urls.add(i);
			}
			if (urls.size() == 4)
				return urls;
		}
		return urls;
	}
}
