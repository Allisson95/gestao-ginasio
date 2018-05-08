package br.com.gestaoginasio.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.gestaoginasio.model.Usuario;
import br.com.gestaoginasio.repository.UsuarioRepository;

@Service
public class DetalhesUsuarioService implements UserDetailsService {

	@Inject
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
		Usuario usuarioSalvo = this.usuarioRepository.buscarUsuarioPelaMatricula(matricula);
		return new UsuarioSistema(usuarioSalvo, authorities(usuarioSalvo));
	}

	private Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(
				p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().getDescricao().toUpperCase())));
		return authorities;
	}

}
