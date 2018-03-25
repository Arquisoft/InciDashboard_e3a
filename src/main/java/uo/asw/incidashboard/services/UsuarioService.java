package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	private IncidenciasService incidenciasService;
	
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
		int contadorT = Integer.MAX_VALUE;
		Usuario userA = null;
		for (Usuario u : usuarios) {
			if (u.getPerfil().equals(PerfilTipos.OPERARIO)) {
				 userA = u;
			}
		}
		for (Usuario u : usuarios) {
			if (u.getPerfil().equals(PerfilTipos.OPERARIO)) {
				List<Incidencia> incidencias = incidenciasService.getIncidencias();
				int contadorU = 0;
				for (Incidencia i : incidencias) {
					if (i.getEstado().equals(EstadoTipos.ABIERTA)) {
						contadorU++;
					}
				}
				if (contadorU < contadorT) {
					userA = u;
				}

			}
		}
		return userA;
	}

}
