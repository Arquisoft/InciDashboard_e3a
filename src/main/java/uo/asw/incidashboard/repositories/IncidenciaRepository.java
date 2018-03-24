package uo.asw.incidashboard.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uo.asw.dbManagement.model.Incidencia;


public interface IncidenciaRepository extends CrudRepository<Incidencia, Long> {

	@Query("SELECT i FROM Incidencia i WHERE i.agente.id = ?1 ORDER BY i.id ASC ")
	Page<Incidencia> findIncidenciasByIdAgent(Pageable pageable, Long id);
	
	@Query("SELECT i FROM Incidencia i WHERE i.operario.identificador = ?1 ORDER BY i.id ASC ")
	Page<Incidencia> findIncidenciasByIdUser(Pageable pageable, Long identificador);
	
	@Query("SELECT i FROM Incidencia i WHERE i.nombreIncidencia = ?1 ")
	Incidencia findIncidenciByName(String nombre);

}
