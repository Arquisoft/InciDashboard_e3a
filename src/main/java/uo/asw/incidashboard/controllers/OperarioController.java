package uo.asw.incidashboard.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import uo.asw.dbManagement.model.Agente;
import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.services.IncidenciaService;
import uo.asw.incidashboard.services.UsuarioService;


public class OperarioController {
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private IncidenciaService incidenciaService;
	
	@RequestMapping("/operarios/operario" )
	public String getListado(Model model, Pageable pageable, Principal principal){
		
		String identificador = principal.getName();
		Usuario user = usuarioService.getUsuarioByIdentificador(identificador);
		
		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		
		
		incidencias = incidenciaService.getUserIncidencias(pageable, user.getId());
		
		model.addAttribute("incidenciasList", incidencias.getContent() );
		model.addAttribute("nameUser", "Incidencias de "+user.getNombre());
		model.addAttribute("page", incidencias);
		
		return "/operarios/operario";
	}

}
