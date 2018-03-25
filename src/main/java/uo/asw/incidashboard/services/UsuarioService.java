package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bcPass;

	@PostConstruct
	public void init() {
	}

	public List<Usuario> getUsuarios() {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usersRepository.findAll().forEach(usuarios::add);
		return usuarios;
	}

	public Usuario getUsuarioByIdentificador(String identificador) {
		return usersRepository.findByIdentificador(identificador);
	}

	public Usuario getUsuarioByMail(String mail) {
		return usersRepository.findByEmail(mail);
	}

	public void addUsuario(Usuario usuario) {
		usuario.setContrasena(bcPass.encode(usuario.getContrasena()));
		usersRepository.save(usuario);
	}

	public void deleteUser(ObjectId id) {
		usersRepository.delete(id);
	}
	public Usuario getUsuario(ObjectId objectId) {
		return usersRepository.findOne(objectId);
	}
}
