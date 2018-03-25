package uo.asw.incidashboard.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uo.asw.dbManagement.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {

	@Query("SELECT i FROM Usuario i WHERE i.identificador = ?1 ORDER BY i.id ASC ")
	Usuario findByIdentificador(String identificador);

	@Query("SELECT i FROM Usuario i WHERE i.email = ?1  ORDER BY i.id ASC")
	Usuario findByEmail(String email);

}
