package br.com.gestaoginasio.auth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.gestaoginasio.model.Pessoa;

public class DetalhesUsuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String matricula;
	private LocalDate dtMatricula;
	private String senha;
	private Pessoa pessoa;
	private Collection<? extends GrantedAuthority> permissoes = new ArrayList<>(0);

	public DetalhesUsuario(String matricula, LocalDate dtMatricula, String senha, Pessoa pessoa,
			Collection<? extends GrantedAuthority> permissoes) {
		this.matricula = matricula;
		this.dtMatricula = dtMatricula;
		this.senha = senha;
		this.pessoa = pessoa;
		this.permissoes = permissoes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissoes;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return matricula;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return getPessoa().getAtivo();
	}

	public LocalDate getDtMatricula() {
		return dtMatricula;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

}
