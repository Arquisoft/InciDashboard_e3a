package uo.asw.incidashboard.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.dbManagement.tipos.EstadoTipos;
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
		List<Incidencia> incis = incidenciasService.getAllIncidencias();
		Map<Usuario, Integer> cont = new HashMap<Usuario, Integer>();
		for(Incidencia i: incis) {
			if(i.getEstado() == EstadoTipos.ABIERTA) {
				if(cont.get(i.getOperario()) == null)
					cont.put(i.getOperario(), 1);
				else
					cont.put(i.getOperario(), cont.get(i.getOperario()) + 1);
			}
		}
		int contador = -1;
		Usuario user = null;
		for (Entry<Usuario, Integer> entry : cont.entrySet()) {
		    if(contador == -1) {
		    	contador = entry.getValue();
		    	user = entry.getKey();
		    }
		    else {
		    	if(contador > entry.getValue()) {
		    		contador = entry.getValue();
		    		user = entry.getKey();
		    	}
		    }
		}
		return user;
	}

	public void deleteAll() {
		usersRepository.deleteAll();
	}

	public Page<Usuario> findAll(Pageable pageable) {
		return usersRepository.findAll(pageable);
	}
}
