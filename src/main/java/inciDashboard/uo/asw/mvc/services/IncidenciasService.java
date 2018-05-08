package inciDashboard.uo.asw.mvc.services;

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

import inciDashboard.uo.asw.dbManagement.model.Incidencia;
import inciDashboard.uo.asw.dbManagement.model.Propiedad;
import inciDashboard.uo.asw.dbManagement.model.Usuario;
import inciDashboard.uo.asw.dbManagement.model.ValorLimite;
import inciDashboard.uo.asw.dbManagement.tipos.EstadoTipos;
import inciDashboard.uo.asw.dbManagement.tipos.PerfilTipos;
import inciDashboard.uo.asw.dbManagement.tipos.PropiedadTipos;
import inciDashboard.uo.asw.mvc.repositories.IncidenciaRepository;

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

	/**
	 * Añade las incidencias recibidas desde kafka en una lista especial
	 * llamada lInciKafka, ademas, les asigna un operario.
	 * @param incidencia recibida desde kafka
	 */
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

	/**
	 * comprueba la incidencia para notificarle al operario
	 * que la tenga adjudicada de que ha recibido una nueva incidencia
	 * que resolver
	 * @param incidencia que se le notifica al operario que tiene asginada
	 */
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
	
	/**
	 * Elimina una incidencia de las notificaciones del operario que la tiene asignada
	 * @param id de la incidencia
	 */
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

	/**
	 * Devuelve la la lista lInciKafka pero con valores del atributo
	 * critics incializados
	 * @return lista de incidencias
	 */
	public List<Incidencia> getLInciKafka() {
		return setCritics(lInciKafka);
	}

	/**
	 * Se encarga de asignarle al operario que tiene menos incidencias
	 * la incidencia pasada como parametro.
	 * @param incidencia a la que asignar operario
	 * @return la incidencia con el operario asignado
	 */
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

	/**
	 * Devuelve las incidencias asignadas a un operario
	 * @param pageable  bootstrap
	 * @param u del que se quiere conocer sus incidencias asignadas
	 * @return lista de incidencias asignadas al operario
	 */
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
	
	/**
	 * Modifica una propiedad en todas las incidenas que la tienen
	 * @param property Propiedad a modificar en todas las incidencias
	 */
	public void modifyProperties(Propiedad property) {
		List<Incidencia> incis = incidenciaRepository.findAll();
		for (Incidencia i : incis) {
			modifyPropInci(i, property);
			incidenciaRepository.save(i);
		}
	}

	/**
	 * Compara la si la incidencia tiene la propiedad y si es correcto
	 * guarda la nueva propiedad
	 * @param i incidencia en la que se comprueba si tiene la propiedad
	 * @param property nueva
	 */
	private void modifyPropInci(Incidencia i, Propiedad property) {
		for (Propiedad p : i.getPropiedades()) {
			if (p.getPropiedad() == property.getPropiedad()) {
				pService.addPropiedad(p);
			}
		}
	}

	/**
	 * Obtiene los datos del grafico circular de la vista del analista
	 * @return una lista con los datos del grafico circular
	 */
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

	/**
	 * Calcular el porcentaje para cada estado que hay en todas las incidencias
	 * @param state del que se quiere saber el porcentaje
	 * @param inci lista de incidencias en las que hacer los calculos
	 * @return % de aparcicion del estado en las incidencias
	 */
	private Double calculatePercentage(EstadoTipos state, List<Incidencia> inci) {
		List<Double> values = new ArrayList<Double>();
		Double aux = 0.0;
		for (Incidencia i : inci) {
			if (i.getEstado() == state)
				values.add(aux++);
		}
		return (values.size() * 100.0) / inci.size();
	}

	/**
	 * Obtiene las incidencias desde hace 10 dias hasta hoy, las guarda en un
	 * map con clave fecha y valor un numero entero con las incidencias de esas 
	 * fecgas
	 * @param incidencias en las que buscar
	 * @return un map con las fechas ordenadas y el numero de incidencias en
	 * esa fecha
	 */
	private Map<String, Integer> getIncidencias10dias(List<Incidencia> incidencias) {
		Map<String, Integer> datos = new HashMap<>();
		Date hoy = new Date();

		Calendar c1 = Calendar.getInstance();
		c1.setTime(hoy);

		datos.put((new SimpleDateFormat("yyyy/MM/dd")).format(hoy), 0);
		for (int i = 0; i < 9; i++) {
			c1.add(Calendar.DAY_OF_MONTH, -1);
			Date hace10dias = c1.getTime();
			datos.put((new SimpleDateFormat("yyyy/MM/dd")).format(hace10dias), 0);
			c1.setTime(hace10dias);
		}

		for (Incidencia inci : incidencias) {
			String f = (new SimpleDateFormat("yyyy/MM/dd")).format(inci.getFechaEntrada());
			if (datos.get(f) != null) {
				datos.put(f, (datos.get(f) + 1));
			}
		}

		return datos;
	}

	/**
	 * Utiliza getIncidencias10dias para obtener el map que genera y asi añadir
	 * las fechas en una lista.
	 * @param incidencias de las que se quiere obtener las fechas
	 * @return una lista con las fechas de las incidendias
	 */
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

	/**
	 * Utilzia getIncidencias10dias para obtener el numero de incidencias
	 * que se obtienen de la llamada a dicho metodo
	 * @param incidencias de las que se quiere obtener el numero
	 * @return una lista de enteros con el numero de incidencias
	 */
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
	
	/**
	 * cambia el estado de una incidencia, por el estado
	 * pasado como parametro
	 * @param id de la incidencia de la que se quiere cambiar el estado
	 * @param nuevoEstado que adquirirá la incidenciaS
	 */
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

	/**
	 * Comprueba que las propiedades de la incidencia que se pasa como parametro
	 * tenga propiedades que estan dentro de sus valores limites permitidos
	 * @param i
	 * @return
	 */
	public boolean incidenciaConValorLimite(Incidencia i) {
		for (Propiedad p : i.getPropiedades()) {
			ValorLimite vL = vLimService.findByPropiedad(p.getPropiedad().toString());
			if (vL != null) {
				return (vL.isMaxCritico() && (p.getValor() >= vL.getValorMax()))
						|| (vL.isMinCritico() && (p.getValor() <= vL.getValorMin()));
			}
		}
		return false;
	}

	/**
	 * Modifica el atributo Critics de las incidencias pasadas como parametro
	 * en funcion de si sus propiedades superan el valor limite permito que tienen
	 * asignado
	 * @param incis a las que se quiere cambiar el atributo
	 * @return a lista con el atributo modificado
	 */
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

	/**
	 * Este metodo devuelve una lista con las url de las imagenes de las ultimas 4
	 * incidencias con imagenes OJO: no guardo en BD el setImageURL Para modificar
	 * el nombreImg voy desde el principio del nombre, hasta queempieza el jpg
	 * 
	 * @param Todas
	 *            las incidencias que van a la vista
	 * @return las Url a las que se debe acceder desde la vista para ver las
	 *         imagenes de las incidencias
	 */
	public List<Incidencia> getUrlImgs(List<Incidencia> incidencias) {
		List<Incidencia> urls = new ArrayList<Incidencia>();
		String urlBase = "http://18.237.112.43:8090/incidencia/";
		for(int i = incidencias.size() - 1; i >= 0; i--){
			if (incidencias.get(i).getImageURL() != null) { 
				if(!incidencias.get(i).getImageURL().equals("/img/post/")) {
					String nombreImg = incidencias.get(i).getImageURL().substring(10, incidencias.get(i).getImageURL().length() - 4);
					incidencias.get(i).setImageURL(urlBase + nombreImg);
					urls.add(incidencias.get(i));
				}
			}
			if (urls.size() == 4)
				return urls;
		}
		return urls;
	}

	/**
	 * Devuelve una lista de String[] con las fechas en formato MM/dd/hh:mm
	 * de las lista de incidencias que se pasa, pero solo de las últimas numInci
	 * y de aquellas que tengan la propiedad pasada como parámetro.
	 * El metodo se utiliza para llenar las graficas del analista.
	 * @param La lista de incidencias de la que se desea obtener las fechas
	 * @param La propiedad a buscar en las incidencias
	 * @param El numero de las ultimas incidencias que queremos tener en cuenta
	 * @return Una lista de String[] con las fechas de las incidencias
	 */
	public String[] getDateProperty(List<Incidencia> incidencias, PropiedadTipos tp, int numInci) {
		List<String> aux = new ArrayList<String>();
		int startLoop = numInci > incidencias.size() ? 0 : incidencias.size() - numInci;
		for(int i = startLoop; i < incidencias.size(); i++) {
			Propiedad p = incidencias.get(i).getPropertyByType(tp);
			if(p != null) {
				Calendar c1 = Calendar.getInstance(); c1.setTime(incidencias.get(i).getFechaEntrada());
				aux.add(String.valueOf((new SimpleDateFormat("MM/dd/hh:mm")).format(c1.getTime())));
			}
		}
		return aux.stream().toArray(String[]::new);
	}

	/**
	 * Devuelve un map que tiene 2 claves, una 'yAxis' y la otra 'height'.
	 * yAxis me devuelve una lista Double[] con los valores de la propiedad pasada como parametro
	 * en las ultimas numIncis de la lista de incidencias.
	 * height me devuelve de la lista yAxis el valor mas alto, esto se utiliza para marcar
	 * el punto mas alto en las graficas.
	 * @param Incidencias de las que se desea obtener valores 
	 * @param La propiedad de la que deseamos obtener los valores en las incidencias
	 * @param El numero de las ultimas incidencias que queremos tener en cuenta
	 * @return Un map que contiene los valores del eje 'y' y la altura maxima del grafico
	 */
	public Map<String, Double[]> infoGraphics(List<Incidencia> incidencias, PropiedadTipos tp, int numInci) {
		List<Double> aux = new ArrayList<Double>();
		Map<String, Double[]> ret = new HashMap<String, Double[]>();
		int startLoop = numInci > incidencias.size() ? 0 : incidencias.size() - numInci;
		double maxValue = 0;
		for(int i = startLoop; i < incidencias.size(); i++) {
			Propiedad p = incidencias.get(i).getPropertyByType(tp);
			if(p != null) {
				aux.add(p.getValor());
				if(maxValue < p.getValor())
					maxValue = p.getValor();
			}
		}
		ret.put("yAxis", aux.stream().toArray(Double[]::new));
		ret.put("height", new Double[] {maxValue});
		return ret;
	}
	

}
