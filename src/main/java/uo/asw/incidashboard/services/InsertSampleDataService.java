package uo.asw.incidashboard.services;



import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class InsertSampleDataService {

	// Se inyectan servicios con @Autowired
	
	@PostConstruct
	public void init() {
		/*Aquí se crean los objetos y y luego se llama al 
		 * service oportuno para añadirlo al repositorio, en el
		 * service solo debe haber un metodo que llame al save de repository,
		 * que ya esta por Crud*/

	}

}
