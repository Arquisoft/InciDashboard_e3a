package uo.asw.incidashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import uo.asw.incidashboard.services.IncidenciasService;

@Controller
public class IncidenciasController {

	
	@Autowired
	private IncidenciasService incidenciaService;
	
	
	@RequestMapping("/inci/cargar")
	public String cargarIncidencias(Model model) {
		incidenciaService.recogerNuevasIncidencias();
		return "redirect:/home";
	}
}
