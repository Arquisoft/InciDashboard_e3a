package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.EstadoTipos;
import uo.asw.dbManagement.tipos.PerfilTipos;
import uo.asw.incidashboard.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bcPass;

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

	public Usuario getUsuarioConMenosIncis() {
		List<Usuario> usuarios = getUsuarios();
		int contador = 0;
		int contadorMin = Integer.MAX_VALUE;
		Usuario userMenor = null;
		for (int i = 0; i < usuarios.size(); i++) {
			contador = 0;
			for (Incidencia inc : usuarios.get(i).getIncidencias()) {
				if (usuarios.get(i).getPerfil().equals(PerfilTipos.OPERARIO)) {
					if (inc.getEstado().equals(EstadoTipos.ABIERTA)) {
						contador++;
					}
					if (contador < contadorMin) {
						contadorMin = contador;
						userMenor = usuarios.get(i);
					}
				}
			}
		}
		return userMenor;

	}
}
