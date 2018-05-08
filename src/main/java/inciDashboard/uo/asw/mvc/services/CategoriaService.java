package inciDashboard.uo.asw.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inciDashboard.uo.asw.dbManagement.model.Categoria;
import inciDashboard.uo.asw.mvc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository c;

	public void addCategoria(Categoria c1) {
		c.save(c1);
	}
	
	public void deleteAll() {
		c.deleteAll();
	}

}
