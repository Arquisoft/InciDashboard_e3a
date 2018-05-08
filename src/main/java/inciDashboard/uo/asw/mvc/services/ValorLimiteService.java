package inciDashboard.uo.asw.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import inciDashboard.uo.asw.dbManagement.model.ValorLimite;
import inciDashboard.uo.asw.mvc.repositories.ValorLimiteRepository;

@Service
public class ValorLimiteService {
	@Autowired
	private ValorLimiteRepository valorLimiteRepository;

	public void addValorLimite(ValorLimite v1) {
		valorLimiteRepository.save(v1);
	}

	public Page<ValorLimite> findAll(Pageable pageable) {
		return valorLimiteRepository.findAll(pageable);
	}

	public void deleteAll() {
		valorLimiteRepository.deleteAll();
	}

	/**
	 * Actualiza el valor limite correspondiente a la propiedad pasada como parametro con los 
	 * valores que se reciben.
	 * @param propiedad de asociada al valor limite
	 * @param valorMaximo String
	 * @param valorMinimo String
	 * @param criticoMax String
	 * @param criticoMin String
	 */
	public void update(String propiedad, String valorMaximo, String valorMinimo, 
			String criticoMax, String criticoMin) {
		ValorLimite vL = valorLimiteRepository.findByPropiedad(propiedad);
		
		if(criticoMax.equals("true")) {
			vL.setMaxCritico(true);
		}else vL.setMaxCritico(false);
		
		if(criticoMin.equals("true")) {
			vL.setMinCritico(true);
		}else vL.setMinCritico(false);
		
		
		vL.setValorMax(Double.parseDouble(valorMaximo));
		vL.setValorMin(Double.parseDouble(valorMinimo));
		valorLimiteRepository.save(vL);
	}
	
	public ValorLimite findByPropiedad(String propiedad) {
		return valorLimiteRepository.findByPropiedad(propiedad);
	}
}
