package inciDashboard.uo.asw.mvc.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import inciDashboard.uo.asw.dbManagement.model.Usuario;

@EnableMongoRepositories
public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {

	Usuario findByIdentificador(String identificador);
	
	Usuario findByEmail(String email);

}
