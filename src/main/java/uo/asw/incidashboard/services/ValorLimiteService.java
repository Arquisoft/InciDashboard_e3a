package uo.asw.incidashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Propiedad;
import uo.asw.dbManagement.model.ValorLimite;
import uo.asw.incidashboard.repositories.PropiedadRepository;
import uo.asw.incidashboard.repositories.ValorLimiteRepository;

@Service
public class ValorLimiteService {
	@Autowired
	private ValorLimiteRepository v;

	public void addValorLimite(ValorLimite v1) {
		v.save(v1);
	}

	public Page<ValorLimite> findAll(Pageable pageable) {
		return v.findAll(pageable);
	}

	public void deleteAll() {
		v.deleteAll();
	}
}
