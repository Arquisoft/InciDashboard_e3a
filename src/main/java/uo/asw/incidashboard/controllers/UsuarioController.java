package uo.asw.incidashboard.controllers;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Propiedad;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.services.IncidenciasService;
import uo.asw.incidashboard.services.PropiedadesService;
import uo.asw.incidashboard.services.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private IncidenciasService incidenciaService;

	@Autowired
	private PropiedadesService propiedadesService;
	
	
	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping("/users/admin")
	public String getListado(Model model, Pageable pageable, Principal principal) {
		Page<Usuario> operarios = new PageImpl<Usuario>(new LinkedList<Usuario>());
		operarios = usuarioService.findAll(pageable);
		
		model.addAttribute("operariosList", operarios.getContent());
		model.addAttribute("page", operarios);
		model.addAttribute("conectado", "operario4@prueba.es");
		return "/users/admin";
	}

	@RequestMapping(value = "/users/admin", method = RequestMethod.POST)
	public String getAdmin(Model model, @ModelAttribute Propiedad property) {
		incidenciaService.modifyProperties(property);
		return "redirect:/users/admin";
	}

	@RequestMapping("/users/operario")
	public String getOperarios(Model model, Pageable pageable, Principal principal) {
		String mail = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario user = usuarioService.getUsuarioByMail(mail);
		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		incidencias = incidenciaService.getUserIncidencias(pageable, user);
		model.addAttribute("incidenciasList", incidencias.getContent());
		model.addAttribute("nameUser", "Incidencias de " + user.getNombre());
		model.addAttribute("page", incidencias);
		return "/users/operario";
	}

	@RequestMapping("/users/analisis")
	public String getResponsable(Model model, Pageable pageable, Principal principal) {
		
		List<Incidencia> incidencias = incidenciaService.getAllIncidencias();
		model.addAttribute("incidenciasList", incidencias);
		model.addAttribute("page", incidencias);
		model.addAttribute("datos", incidenciaService.getNum(incidencias));
		model.addAttribute("fechas", incidenciaService.getDays(incidencias));
		
		int numMax =0;
		for(int i=0;i<incidenciaService.getNum(incidencias).length;i++) {
			if(numMax < incidenciaService.getNum(incidencias)[i]) numMax = incidenciaService.getNum(incidencias)[i];
		}
		model.addAttribute("max", numMax+2);
		model.addAttribute("gCircular", incidenciaService.getDataCircle());
		return "/users/analisis";
	}

}
