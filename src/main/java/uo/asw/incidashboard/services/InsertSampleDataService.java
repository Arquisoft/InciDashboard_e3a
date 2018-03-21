package uo.asw.incidashboard.services;



import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Agente;
import uo.asw.dbManagement.model.Categoria;
import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Propiedad;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.CategoriaTipos;
import uo.asw.dbManagement.tipos.PerfilTipos;
import uo.asw.dbManagement.tipos.PropiedadTipos;




@Service
public class InsertSampleDataService {

	// Se inyectan servicios con @Autowired
	@Autowired
	private AgenteService agenteService;
	
	@Autowired
	private IncidenciaService incidenciaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostConstruct
	public void init() {
		/*Aquí se crean los objetos y y luego se llama al 
		 * service oportuno para añadirlo al repositorio, en el
		 * service solo debe haber un metodo que llame al save de repository,
		 * que ya esta por Crud*/
		// Creación de agentes
		Agente agente1 = new Agente("Agente1", "123456", "1", "Id1", "Lat1", "Lon1", "agente1@prueba.es","si"); 
		Agente agente2 = new Agente("Agente2", "123456", "2", "Id2", "Lat2", "Lon2", "agente2@prueba.es","si");
		Agente agente3 = new Agente("Agente3", "123456", "3", "Id3", "Lat3", "Lon3", "agente3@prueba.es","si");
		Agente agente4 = new Agente("Agente4", "123456", "4", "Id4", "Lat4", "Lon4", "agente4@prueba.es","si");
		Agente agente5 = new Agente("Agente5", "123456", "5", "Id5", "Lat5", "Lon5", "agente5@prueba.es","si");
		Agente agente6 = new Agente("Agente6", "123456", "6", "Id6", "Lat6", "Lon6", "agente6@prueba.es","si");
		
		// Guardado de agentes
		agenteService.addAgente(agente1);
		agenteService.addAgente(agente2);
		agenteService.addAgente(agente3);
		agenteService.addAgente(agente4);
		agenteService.addAgente(agente5);
		agenteService.addAgente(agente6);
		
		// Creación de operarios
		Usuario op1 = new Usuario("nombre1", "apellido1", "operario1@prueba.es", "Id1", "123456", PerfilTipos.OPERARIO);
		Usuario op2 = new Usuario("nombre2", "apellido2", "operario2@prueba.es", "Id2", "123456", PerfilTipos.OPERARIO);
		Usuario op3 = new Usuario("nombre3", "apellido3", "operario3@prueba.es", "Id3", "123456", PerfilTipos.OPERARIO);
		usuarioService.addUsuario(op1);
		usuarioService.addUsuario(op2);
		usuarioService.addUsuario(op3);
		
		
		// Creación de propiedades 
		Propiedad p1 = new Propiedad(PropiedadTipos.TEMPERATURA, null, 100.0); /* ¿UNIDADES? */
		Propiedad p2 = new Propiedad(PropiedadTipos.HUMEDAD, null, 90.0);
		Propiedad p3 = new Propiedad(PropiedadTipos.PRESION, null, 1.1);
		Propiedad p4 = new Propiedad(PropiedadTipos.VELOCIDAD_CIRCULACION, null, 110.0);
		Propiedad p5 = new Propiedad(PropiedadTipos.VELOCIDAD_VIENTO, null, 120.0);
		Propiedad p6 = new Propiedad(PropiedadTipos.VALOR_NO_ASIGNADO, null, 0.0);
		
		// Categorias 
		Categoria c1 = new Categoria(CategoriaTipos.ACCIDENTE_AEREO, null);
		Categoria c2 = new Categoria(CategoriaTipos.ACCIDENTE_CARRETERA, null);
		Categoria c3 = new Categoria(CategoriaTipos.FUEGO, null);
		Categoria c4 = new Categoria(CategoriaTipos.INUNDACION, null);
		Categoria c5 = new Categoria(CategoriaTipos.METEOROLOGICA, null);
		Categoria c6 = new Categoria(CategoriaTipos.VALOR_NO_ASIGNADO, null);
		
		// Creación de fechas 
		Calendar Choy = Calendar.getInstance();
		Calendar CunaSemana = Calendar.getInstance(); CunaSemana.add(Calendar.DAY_OF_MONTH, 7); 
		
		// Set de propiedades
		Set<Propiedad> propiedades1 = new HashSet<Propiedad>();
		propiedades1.add(p1);
		propiedades1.add(p2);
		Set<Propiedad> propiedades2 = new HashSet<Propiedad>();
		propiedades1.add(p3);
		propiedades1.add(p4);
		Set<Propiedad> propiedades3 = new HashSet<Propiedad>();
		propiedades1.add(p5);
		Set<Propiedad> propiedades4 = new HashSet<Propiedad>(); 
		propiedades2.add(p6);

		// Set de categorias
		Set<Categoria> categorias1 = new HashSet<Categoria>(); 
		categorias1.add(c1);
		categorias1.add(c2);
		Set<Categoria> categorias2 = new HashSet<Categoria>(); 
		categorias1.add(c3);
		categorias1.add(c4);
		Set<Categoria> categorias3 = new HashSet<Categoria>(); 
		categorias1.add(c5);
		Set<Categoria> categorias4 = new HashSet<Categoria>();
		categorias2.add(c6);
		
		//Incidencias
		/* PROPIEDADES = TEMPERATURA, HUMEDAD
		 * CATEGORIAS = ACCIDENTE_AEREO, ACCIDENTE_CARRETERA
		 * ESTADO = ABIERTA - SIN OPERARIO */
		Incidencia inci1 = new Incidencia("Inci1", "descripcion1", "Lat1", "Lon1", Choy.getTime(),
				CunaSemana.getTime(), agente1, propiedades1 , categorias1);
		incidenciaService.addIncidencia(inci1);
		
		/* PROPIEDADES = PRESION, VELOCIDAD_CIRCULACION
		 * CATEGORIAS = FUEGO, INUNDACION
		 * ESTADO = ABIERTA - SIN OPERARIO */
		Incidencia inci2 = new Incidencia("Inci2", "descripcion2", "Lat2", "Lon2",  Choy.getTime(),
				CunaSemana.getTime(), agente1, propiedades2 , categorias2);
		incidenciaService.addIncidencia(inci2);
		
		/* PROPIEDADES = VELOCIDAD_VIENTO
		 * CATEGORIAS = METEOROLOGICA 
		 * ESTADO = EN PROCESO */
		Incidencia inci3 = new Incidencia("Inci3", "descripcion3", "Lat3", "Lon3",Choy.getTime(),
				CunaSemana.getTime(), agente1, propiedades3 , categorias3);
		inci3.asignarOperario(op1);
		incidenciaService.addIncidencia(inci3);
		
		/* PROPIEDADES = VELOCIDAD_VIENTO
		 * CATEGORIAS = VALOR_NO_ASIGNADO
		 * ESTADO = EN PROCESO*/
		Incidencia inci4 = new Incidencia("Inci4", "descripcion4", "Lat4", "Lon4",  Choy.getTime(),
				CunaSemana.getTime(), agente4, propiedades3 , categorias4);
		inci3.asignarOperario(op2);
		incidenciaService.addIncidencia(inci4);
		
		/* PROPIEDADES = VALOR_NO_ASIGNADO
		 * CATEGORIAS = METEOROLOGICA 
		 * ESTADO = EN PROCESO*/
		Incidencia inci5 = new Incidencia("Inci5", "descripcion5", "Lat5", "Lon5", Choy.getTime(),
				CunaSemana.getTime(), agente4, propiedades4 , categorias3);
		inci3.asignarOperario(op3);
		incidenciaService.addIncidencia(inci5);
		
		/* PROPIEDADES = VALOR_NO_ASIGNADO
		 * CATEGORIAS = VALOR_NO_ASIGNADO
		 * ESTADO = EN PROCESO*/
		Incidencia inci6 = new Incidencia("Inci5", "descripcion", "Lat5", "Lon5", Choy.getTime(),
				CunaSemana.getTime(), agente4, propiedades4 , categorias4);
		inci3.asignarOperario(op3);
		incidenciaService.addIncidencia(inci6);
		
		/* PROPIEDADES = TEMPERATURA, HUMEDAD
		 * CATEGORIAS = FUEGO, INUNDACION
		 * ESTADO = EN PROCESO*/
		Incidencia inci7 = new Incidencia("Inci6", "descripcion6", "Lat6", "Lon6",  Choy.getTime(),
				CunaSemana.getTime(), agente1, propiedades1 , categorias2);
		inci7.asignarOperario(op1);
		inci7.cerrarIncidencia();
		incidenciaService.addIncidencia(inci7);
		
		/* PROPIEDADES = PRESION, VELOCIDAD_CIRCULACION
		 * CATEGORIAS = ACCIDENTE_AEREO, ACCIDENTE_CARRETERA
		 * ESTADO = ANULADA*/
		Incidencia inci8 = new Incidencia("Inci7", "descripcion1", "Lat7", "Lon7", Choy.getTime(),
				CunaSemana.getTime(), agente1, propiedades2 , categorias1);
		inci8.asignarOperario(op2);
		inci8.anularIncidencia();
		incidenciaService.addIncidencia(inci8);

	}

}
