package uo.asw.incidashboard.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import uo.asw.dbManagement.model.Usuario;


public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByIdentificador(String identificador);

}
