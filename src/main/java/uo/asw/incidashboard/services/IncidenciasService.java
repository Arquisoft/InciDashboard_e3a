package uo.asw.incidashboard.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;

import org.bson.types.ObjectId;
import org.omg.CORBA.portable.IndirectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Categoria;
import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Propiedad;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.CategoriaTipos;
import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.dbManagement.tipos.PropiedadTipos;
import uo.asw.incidashboard.repositories.IncidenciaRepository;
import uo.asw.incidashboard.util.DateUtil;

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
	
	public void deleteAll() {
		incidenciaRepository.deleteAll();
	}

	public void modifyProperties(Propiedad property) {
		List<Incidencia> incis = incidenciaRepository.findAll();
		for(Incidencia i: incis) {
			modifyPropInci(i,property);
			incidenciaRepository.save(i);
		}
	}

	private void modifyPropInci(Incidencia i, Propiedad property) {
		for(Propiedad p: i.getPropiedades()) {
			if(p.getPropiedad() == property.getPropiedad()) {
			
				pService.addPropiedad(p);
			}
		}
	}
	
	/**
		 * Simula de forma muy simple la llegada de nuevas incidencias
		 */
		public void recogerNuevasIncidencias() {
			int numeroIncis= incidenciaRepository.findAll().size();
			// ID's agentes
					String idAgente1 = "Id1";
					String idAgente2 = "Id2";

					// Creación de propiedades
					Propiedad p1 = new Propiedad(PropiedadTipos.TEMPERATURA, 100.0); /* ¿UNIDADES? */
					Propiedad p2 = new Propiedad(PropiedadTipos.HUMEDAD, 90.0);
					Propiedad p3 = new Propiedad(PropiedadTipos.PRESION, 1.1);
					Propiedad p4 = new Propiedad(PropiedadTipos.VELOCIDAD_CIRCULACION, 110.0);

					// Categorias
					Categoria c1 = new Categoria(CategoriaTipos.ACCIDENTE_AEREO);
					Categoria c2 = new Categoria(CategoriaTipos.ACCIDENTE_CARRETERA);
					Categoria c3 = new Categoria(CategoriaTipos.FUEGO);
					Categoria c4 = new Categoria(CategoriaTipos.INUNDACION);

					// Creación de fechas
					Calendar Choy = Calendar.getInstance();
					Calendar CunaSemana = Calendar.getInstance();
					CunaSemana.add(Calendar.DAY_OF_MONTH, 7);

					// Set de propiedades
					Set<Propiedad> propiedades1 = new HashSet<Propiedad>();
					propiedades1.add(p1);
					propiedades1.add(p2);
					Set<Propiedad> propiedades2 = new HashSet<Propiedad>();
					propiedades1.add(p3);
					propiedades1.add(p4);
			

					// Set de categorias
					Set<Categoria> categorias1 = new HashSet<Categoria>();
					categorias1.add(c1);
					categorias1.add(c2);
					Set<Categoria> categorias2 = new HashSet<Categoria>();
					categorias1.add(c3);
					categorias1.add(c4);
			

					// Incidencias
					/*
					 * PROPIEDADES = TEMPERATURA, HUMEDAD CATEGORIAS = ACCIDENTE_AEREO,
					 * ACCIDENTE_CARRETERA ESTADO = ABIERTA - SIN OPERARIO
					 */
					Incidencia inci1 = new Incidencia("Inci"+(numeroIncis+1), "descripcion"+(numeroIncis+1), "Lat"+(numeroIncis+1), "Lon"+(numeroIncis+1), Choy.getTime(), CunaSemana.getTime(),
							idAgente1, propiedades1, categorias1);

					/*
					 * PROPIEDADES = PRESION, VELOCIDAD_CIRCULACION CATEGORIAS = FUEGO,
					 * INUNDACION ESTADO = ABIERTA - SIN OPERARIO
					 */
					Incidencia inci2 = new Incidencia("Inci"+(numeroIncis+2), "descripcion"+(numeroIncis+2), "Lat"+(numeroIncis+2), "Lon"+(numeroIncis+2), Choy.getTime(), CunaSemana.getTime(),
							idAgente2, propiedades2, categorias2);

				
		}
		
		
		public Map<String, Integer> getIncidencias10dias(){
			Map<String, Integer> datos = new HashMap<>();
			Date hoy = new Date();
			
			Calendar c1 = Calendar.getInstance();
			c1.setTime(hoy);
			
			datos.put((new SimpleDateFormat("dd/MM/yyyy")).format(hoy), 0);
			for(int i=0; i<9;i++) {
				c1.add(Calendar.DAY_OF_MONTH, -1);
				Date hace10dias = c1.getTime();
				datos.put((new SimpleDateFormat("dd/MM/yyyy")).format(hace10dias), 0);
				c1.setTime(hace10dias);
			}
			
			for(Incidencia inci: incidenciaRepository.findAll()) {
				String f = (new SimpleDateFormat("dd/MM/yyyy")).format(inci.getFechaEntrada());
				Integer a = datos.get(f);
				if(datos.get(f) != null) {
						datos.put(f, (datos.get(f) +1));	
				}
			}
			
			return datos;
		}
		
		public String[] getDays(){
			Map<String, Integer> datos = getIncidencias10dias();
			List<String> dias = new ArrayList<String>();
			
			
			for (Entry<String, Integer> entry : datos.entrySet()) {
				dias.add(entry.getKey());
			}
			Collections.sort(dias);
			String[] result = new String[dias.size()];
			for(int i=0; i<result.length;i++) {
				result[i] = dias.get(i);
			}
			
			return result;
		}
		
		public int[] getNum(){
			Map<String, Integer> datos = getIncidencias10dias();
			String[] aux = getDays();
					
			int[] result = new int[aux.length];
			
			for(int i=0; i<result.length;i++) {
				result[i] = datos.get(aux[i]);
			}
			
			return result;
		}
}
