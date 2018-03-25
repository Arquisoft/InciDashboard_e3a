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
import uo.asw.incidashboard.services.IncidenciasService;
import uo.asw.incidashboard.services.UsuarioService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String inden) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.getUsuarioByIdentificador(inden);
		if (usuario != null) {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

			return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getContrasena(),
					grantedAuthorities);
		}
		throw new UsernameNotFoundException(inden);
	}

}
