package br.com.gestaoginasio.auth.principal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.gestaoginasio.auth.UsuarioSistema;
import br.com.gestaoginasio.model.Usuario;

/**
 * Bean utilitário que provê métodos que facilitam o uso do objeto
 * <b>Principal</b> do <b>Spring Security</b>. <br />
 * <br />
 * É gerenciado pelo <b>CDI (Weld)</b> e tem scopo de <b>Sessão</b>
 *
 * @author Allisson Alves Batista Nunes
 *
 */
@Named
@SessionScoped
public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;

	private UsuarioSistema principal;

	public Principal() {
		this.principal = (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Usuario getUsuario() {
		return principal.getUsuario();
	}

	public Long getCodigo() {
		return this.principal.getUsuario().getPessoa().getCodigo();
	}

	public String getNome() {
		return this.principal.getUsuario().getPessoa().getNome();
	}

	public String getMatricula() {
		return this.principal.getUsuario().getMatricula();
	}

	public Boolean hasAuthorities(String role) {
		return hasAuthorities(Arrays.asList(role));
	}

	public Boolean hasAuthorities(List<String> roles) {
		Boolean hasAuthority = Boolean.FALSE;
		if (roles != null && !roles.isEmpty()) {
			for (String r : roles) {
				Boolean contains = this.principal.getAuthorities()
						.contains(new SimpleGrantedAuthority(r.toUpperCase()));
				if (contains) {
					hasAuthority = Boolean.TRUE;
					break;
				}
			}
		}
		return hasAuthority;
	}

}
