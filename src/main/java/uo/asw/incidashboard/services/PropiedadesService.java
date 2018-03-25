package uo.asw.incidashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Propiedad;
import uo.asw.incidashboard.repositories.PropiedadRepository;

@Service
public class PropiedadesService {

	@Autowired
	private PropiedadRepository p;

	public void addPropiedad(Propiedad p1) {
		p.save(p1);
	}
	
	
}
