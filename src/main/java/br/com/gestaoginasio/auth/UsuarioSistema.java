package br.com.gestaoginasio.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.gestaoginasio.model.Usuario;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authoritys) {
		super(usuario.getMatricula(), usuario.getSenha(), authoritys);
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
