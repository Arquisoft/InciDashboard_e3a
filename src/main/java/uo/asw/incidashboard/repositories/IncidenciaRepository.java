package uo.asw.incidashboard.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uo.asw.dbManagement.model.Incidencia;

@Repository
public interface IncidenciaRepository extends MongoRepository<Incidencia, ObjectId> {

	@Query("SELECT i FROM incidencias i WHERE i.idAgente = ?1 ORDER BY i.id ASC ")
	Page<Incidencia> findIncidenciasByIdAgent(Pageable pageable, String idAgente);

	@Query("SELECT i FROM incidencias i WHERE i.operario.id = ?1 ORDER BY i.id ASC ")
	Page<Incidencia> findIncidenciasByIdUser(Pageable pageable, ObjectId id);

	@Query("SELECT i FROM incidencias i WHERE i.nombreIncidencia = ?1 ")
	Incidencia findIncidenciByName(String nombre);

	@Query("SELECT i FROM incidencias i ORDER BY i.id ASC ")
	Page<Incidencia> findIncidencias(Pageable pageable);

}
