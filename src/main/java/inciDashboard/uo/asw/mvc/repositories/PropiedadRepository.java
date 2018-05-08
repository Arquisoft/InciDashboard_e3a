package inciDashboard.uo.asw.mvc.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import inciDashboard.uo.asw.dbManagement.model.Propiedad;
@EnableMongoRepositories
public interface PropiedadRepository extends MongoRepository<Propiedad, ObjectId>{

}
