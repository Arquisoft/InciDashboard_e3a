package uo.asw.incidashboard.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import uo.asw.dbManagement.model.Incidencia;

@EnableMongoRepositories
public interface IncidenciaRepository extends MongoRepository<Incidencia, ObjectId> {

	Page<Incidencia> findIncidenciaByIdAgent(String idAgente,Pageable pageable);

	Page<Incidencia> findIncidenciaByIdUser(ObjectId id, Pageable pageable);

	Incidencia findIncidenciabyNombreIncidencia(String nombreIncidencia);

	

}
