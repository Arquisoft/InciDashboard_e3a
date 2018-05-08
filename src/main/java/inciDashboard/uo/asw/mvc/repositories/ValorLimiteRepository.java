package inciDashboard.uo.asw.mvc.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import inciDashboard.uo.asw.dbManagement.model.ValorLimite;

@EnableMongoRepositories
public interface ValorLimiteRepository extends MongoRepository<ValorLimite, ObjectId> {

	ValorLimite findByPropiedad(String propiedad);

}
