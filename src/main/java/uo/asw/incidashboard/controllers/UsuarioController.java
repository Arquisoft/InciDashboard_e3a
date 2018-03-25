package uo.asw.incidashboard.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.services.IncidenciasService;
import uo.asw.incidashboard.services.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private IncidenciasService incidenciaService;

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping("/operarios/administrador")
	public String getListado(Model model, Pageable pageable, Principal principal) {

		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		incidencias = incidenciaService.getIncis(pageable);
		model.addAttribute("incidenciasList", incidencias.getContent());
		model.addAttribute("page", incidencias);

		return "/operarios/administrador";
	}

	@RequestMapping(value = "/operarios/administrador", method = RequestMethod.POST)
	public String getAdmin(Model model, @PathVariable String nombre, @ModelAttribute Incidencia incidencia,
			Principal principal) {
		Incidencia inciOr = incidenciaService.getIncidenciaByName(nombre);
		// modifica valor maximo y minimo
		inciOr.setMinimoValor(incidencia.getMinimoValor());
		inciOr.setMaximoValor(incidencia.getMaximoValor());
		incidenciaService.addIncidencia(inciOr);
		return "redirect:/operarios/administrador";
	}

	@RequestMapping("/operarios/operario")
	public String getOperarios(Model model, Pageable pageable, Principal principal) {

		String mail = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario user = usuarioService.getUsuarioByMail(mail);

		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());

		incidencias = incidenciaService.getUserIncidencias(pageable, user.getId());

		model.addAttribute("incidenciasList", incidencias.getContent());
		model.addAttribute("nameUser", "Incidencias de " + user.getNombre());
		model.addAttribute("page", incidencias);

		return "/operarios/operario";
	}

	@RequestMapping("/operarios/responsableanalisis")
	public String getResponsable(Model model, Pageable pageable, Principal principal) {

		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());

		incidencias = incidenciaService.getIncis(pageable);

		model.addAttribute("incidenciasList", incidencias.getContent());

		model.addAttribute("page", incidencias);

		return "/operarios/responsableanalisis";
	}

}
