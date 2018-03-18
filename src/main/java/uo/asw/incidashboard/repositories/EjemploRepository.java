package uo.asw.repositories;

import org.springframework.data.repository.CrudRepository;

import uo.asw.models.Ejemplo;

//No hace falta poner @Repository porque ya se incluye en CrudRepository
public interface EjemploRepository extends CrudRepository<Ejemplo, Long>{

	// CrudRepository ya incluye metodos de save, delete, findAll, findOne, entre otros
}
