package uo.asw.incidashboard.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uo.asw.dbManagement.model.Propiedad;
@Repository
public interface PropiedadRepository extends MongoRepository<Propiedad, ObjectId>{

}
