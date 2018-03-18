package uo.asw.incidashboard.repositories;

import org.springframework.data.repository.CrudRepository;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;

public interface IncidenciaRepository extends CrudRepository<Incidencia, Long> {

	Incidencia findByNombre(String nombre);
}
