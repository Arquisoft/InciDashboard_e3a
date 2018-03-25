package uo.asw.incidashboard.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.PerfilTipos;
import uo.asw.incidashboard.services.SecurityService;
import uo.asw.incidashboard.services.UsuarioService;

@Controller
public class HomeController {
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private SecurityService secService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	// @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	// public String home(Model model) {
	// return "home";
	// }

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String setEdit(Model model) {
		String mail = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario user = usuarioService.getUsuarioByMail(mail);
		if (user == null)
			return "redirect:/login";
		if (user.getPerfil().equals(PerfilTipos.OPERARIO))
			return "redirect:/operarios/operario";
		else if (user.getPerfil().equals(PerfilTipos.ADMIN_CM))
			return "redirect:/operarios/administrador";
		else if (user.getPerfil().equals(PerfilTipos.ANALISIS_DATOS))
			return "redirect:/operarios/responsableanalisis";
		return "redirect:/login";

	}

}
