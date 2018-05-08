package inciDashboard.uo.asw.mvc.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import inciDashboard.uo.asw.dbManagement.model.Incidencia;
import inciDashboard.uo.asw.dbManagement.model.Usuario;
import inciDashboard.uo.asw.dbManagement.model.ValorLimite;
import inciDashboard.uo.asw.dbManagement.tipos.PropiedadTipos;
import inciDashboard.uo.asw.mvc.services.IncidenciasService;
import inciDashboard.uo.asw.mvc.services.UsuarioService;
import inciDashboard.uo.asw.mvc.services.ValorLimiteService;

@Controller
public class UsuarioController {

	@Autowired
	private IncidenciasService incidenciaService;
	@Autowired
	private ValorLimiteService valorLimiteService;
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		incidenciaService.inicializarListaNotificaciones();
		model.addAttribute("user", new Usuario());	
		return "login";
	}
	
	/**
	 * GET
	 * responde a /users/admin y se encarga de devolver la vista del administrador
	 * donde puede configurar los valores limites.
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param principal bootstrap
	 * @return la vista /users/admin
	 */
	@RequestMapping("/users/admin")
	public String getListado(Model model, Pageable pageable, Principal principal) {
		Page<Usuario> operarios = new PageImpl<Usuario>(new LinkedList<Usuario>());
		operarios = usuarioService.findAll(pageable);
		
		Page<ValorLimite> valoresLimite = new PageImpl<ValorLimite>(new LinkedList<ValorLimite>());
		valoresLimite = valorLimiteService.findAll(pageable);
		
		model.addAttribute("valoresList", valoresLimite.getContent());
		for(ValorLimite vL:valoresLimite) {
			if (vL.getPropiedad().equals("TEMPERATURA")) {
				model.addAttribute("temp", vL);
				break;
			}
		}
		model.addAttribute("usuarioList", operarios.getContent());
		model.addAttribute("page", operarios);
		model.addAttribute("conectado", principal.getName());
		incidenciaService.setUserConnected(principal.getName());
		return "/users/admin";
	}

	/**
	 * POST
	 * respodne a /admin/cambiarUmbral  y se utiliza para cambiar los umbrales de 
	 * las diferentes propiedades de las incidencias.
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param json bootstrap
	 * @return la vista /users/admin
	 */
	@RequestMapping(value = "/admin/cambiarUmbral", method = RequestMethod.POST)
	public String cambiarUmbral(Model model,Pageable pageable, @RequestBody String json) {
		
		String propiedadUmbral = json.split("%2C%22")[0].split("%22%3A%22")[1].split("%22")[0];
		String valorMaximo = json.split("%2C%22")[1].split("%22%3A%22")[1].split("%22")[0];
		String valorMinimo = json.split("%2C%22")[2].split("%22%3A%22")[1].split("%22")[0];
		String criticoMin = json.split("%2C%22")[3].split("%22%3A")[1];
		String criticoMax = json.split("%2C%22")[4].split("%22%3A")[1].split("%7D=")[0];
		valorLimiteService.update(propiedadUmbral, valorMaximo, valorMinimo, criticoMax, criticoMin);
		
		return "redirect:/users/admin";
	}
	
	/**
	 * GET
	 * responde a /users/analiss y se necarga de mostrar la vista del analista con todas
	 * las graficas y datos de incorpora
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param principal bootstrap
	 * @returnla vista /users/analisis
	 */
	@RequestMapping("/users/analisis")
	public String getResponsable(Model model, Pageable pageable, Principal principal) {
		
		List<Incidencia> incidencias = incidenciaService.getAllIncidencias();
		model.addAttribute("incidenciasList", incidencias);
		model.addAttribute("page", incidencias);
		/* Grafico de barras */
		model.addAttribute("datos", incidenciaService.getNum(incidencias));
		model.addAttribute("fechas", incidenciaService.getDays(incidencias));
		/* Imagenes incidencias */
		model.addAttribute("urlImg", incidenciaService.getUrlImgs(incidencias));
		/* INFO GENERICA PARA LAS 3 GRAFICAS*/
		int lastIncis = 25;
		/* Grafica de temperatura */
		Map<String, Double[]> aux = incidenciaService.infoGraphics(incidencias, 
				PropiedadTipos.TEMPERATURA, lastIncis);
		model.addAttribute("dataTemp", aux.get("yAxis"));
		model.addAttribute("fechaTemp", incidenciaService.getDateProperty(incidencias, 
				PropiedadTipos.TEMPERATURA, lastIncis));
		model.addAttribute("maxTemp", aux.get("height")[0].intValue());
		/* Grafica de presion */
		aux = incidenciaService.infoGraphics(incidencias, PropiedadTipos.PRESION, lastIncis);
		model.addAttribute("dataPres", aux.get("yAxis"));
		model.addAttribute("fechaPres", incidenciaService.getDateProperty(incidencias, 
				PropiedadTipos.PRESION, lastIncis));
		model.addAttribute("maxPres", aux.get("height")[0].intValue());
		/* Grafica de humedad */
		aux = incidenciaService.infoGraphics(incidencias, PropiedadTipos.HUMEDAD, lastIncis);
		model.addAttribute("dataHum", aux.get("yAxis"));
		model.addAttribute("fechaHum", incidenciaService.getDateProperty(incidencias, 
				PropiedadTipos.HUMEDAD, lastIncis));
		model.addAttribute("maxHum", aux.get("height")[0].intValue());
		/* Grafico de barras altura max */
		int numMax =0;
		for(int i=0;i<incidenciaService.getNum(incidencias).length;i++) {
			if(numMax < incidenciaService.getNum(incidencias)[i]) numMax = incidenciaService.getNum(incidencias)[i];
		}
		model.addAttribute("max", numMax+2);
		/* Grafico circular*/
		model.addAttribute("gCircular", incidenciaService.getDataCircle());
		incidenciaService.setUserConnected(principal.getName());
		return "/users/analisis";
	}
	
	/**
	 * GET
	 * responde a /refresh-table y se necarga de refrescar la tabla donde llegan 
	 * las incidencias de kafka en la vista del analista, ademas, utiliza fragmentos
	 * @param model bootstrap
	 * @return la actualizacion de /users/analisis
	 */
	@RequestMapping(value="/refresh-table")
	public String getValuesTable(Model model) {
		List<Incidencia> incis = incidenciaService.getLInciKafka();
	    model.addAttribute("dataTable",incis);
	    return "/users/analisis :: #myInLineTable";
	}

	/**
	 * GET
	 * responde a /users/operario y le muestra al operario las incidencias que tiene
	 * asignadas asi como la posibilidad de poder cambiar el estado de estas
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param principal bootstrap
	 * @return la vista /users/operario
	 */
	@RequestMapping("/users/operario")
	public String getOperarios(Model model, Pageable pageable, Principal principal) {
		
		String mail = principal.getName();
		Usuario user = usuarioService.getUsuarioByMail(mail);

		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		incidencias = incidenciaService.getUserIncidencias(pageable, user);
		model.addAttribute("incidenciasList", incidencias.getContent());
		model.addAttribute("nameUser", "          Incidencias de " + user.getNombre());
		model.addAttribute("page", incidencias);
		incidenciaService.setUserConnected(principal.getName());
		return "/users/operario";
	}	 
	
	/**
	 * GET
	 * responde a /users/operario/update y se utiliza para actualizar un fragmento - tabla
	 * de la vista /users/operario
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param principal bootstrap
	 * @return la actualizacion de /users/operario
	 */
	@RequestMapping("/users/operario/update") 
	public String updateListInci(Model model, Pageable pageable, Principal principal){
		String mail = principal.getName();
		Usuario user = usuarioService.getUsuarioByMail(mail);
		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		incidencias = incidenciaService.getUserIncidencias(pageable, user);
		model.addAttribute("incidenciasList", incidencias.getContent());
		model.addAttribute("nameUser", "          Incidencias de " + user.getNombre());
		model.addAttribute("page", incidencias);
		
		model.addAttribute("num", 1);
		incidenciaService.setUserConnected(principal.getName());
		return "/users/operario :: tableInci";
	}
	
	/**
	 * GET
	 * responde a /users/id/cambiarestado/nuevoEstado y se utiliza para que el
	 * operario pueda cambiar de estado las incidencias que tiene asignadas.
	 * @param model bootstrap
	 * @param id de la incidencia de la que se quiere cambiar el estado
	 * @param nuevoEstado de la incidencia
	 * @param principal bootstrap
	 * @return la vista /users/operario
	 */
	@RequestMapping(value="/users/{id}/cambiarEstado/{nuevoEstado}", method=RequestMethod.GET) 
	public String sendResquest(Model model, @PathVariable String id , @PathVariable String nuevoEstado, 
			Principal principal){
		incidenciaService.changeState(id, nuevoEstado);
		return "redirect:/users/operario";
	}
	
	/**
	 * GET
	 * responde a /refresh-alert y se encarga de actualizar el apartado de la vista
	 * donde el operario donde le llegan las aletar de que tiene asignada una 
	 * incidencia
	 * @param model bootstrap
	 * @param pageable bootstrap
	 * @param principal bootstrap
	 * @return la actualizacion de un fragmento en la vista /users/operario
	 */
	@RequestMapping(value="/refresh-alert")
	public String getValuesAlert(Model model, Pageable pageable, Principal principal) {
		List<Incidencia> incis = incidenciaService.getLInciKafka();
		
		 model.addAttribute("incidencias",incis.toArray());
	    model.addAttribute("num",incis.size());
	    Date hoy = new Date();
	    int min = hoy.getMinutes();
	    if(min<=9)
	    	model.addAttribute("fecha","  "+ hoy.getHours() +":0"+min);
	    else
	    	model.addAttribute("fecha","  "+ hoy.getHours() +":"+min);
	    return "/users/operario :: scriptNot";
	}
	
	/**
	 * GET
	 * responde a /eliminar/notificaciones/id aunque borra todas las incidencias
	 * @param model bootstrap
	 * @param id bootstrap
	 * @param principal bootstrap
	 * @return la vista /users/operario/update
	 */
	@RequestMapping(value="/eliminar/notificacion/{id}", method=RequestMethod.GET) 
	public String eliminarNotif(Model model, @PathVariable String id , Principal principal){
		// llamada al método de servici que quitará un elemento de la lista
		return "redirect:/users/operario/update";
	}
}
