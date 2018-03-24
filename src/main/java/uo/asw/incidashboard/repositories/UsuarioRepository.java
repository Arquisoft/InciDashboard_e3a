package uo.asw.incidashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uo.asw.dbManagement.model.Usuario;


public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("SELECT i FROM Usuario i WHERE i.identificador = ?1 ORDER BY i.id ASC ")
	Usuario findByIdentificador(String identificador);
	
	@Query("SELECT i FROM Usuario i WHERE i.email = ?1  ORDER BY i.id ASC")
	Usuario findByEmail(String email);

}
