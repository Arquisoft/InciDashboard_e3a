package uo.asw;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.repositories.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	@Override
	public UserDetails loadUserByUsername(String inden) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByIdentificador(inden);
		if (usuario != null) {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

			return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getContrasena(),
					grantedAuthorities);
		}
		throw new UsernameNotFoundException(inden);
	}


}
