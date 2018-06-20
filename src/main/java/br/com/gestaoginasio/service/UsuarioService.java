package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.time.LocalDate;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.gestaoginasio.model.Permissao;
import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.TipoPermissao;
import br.com.gestaoginasio.model.Usuario;
import br.com.gestaoginasio.repository.PermissaoRepository;
import br.com.gestaoginasio.repository.UsuarioRepository;
import br.com.gestaoginasio.util.Transacional;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Integer semaforo = 0;

	@Inject
	private UsuarioRepository usuarioRepository;
	@Inject
	private PermissaoRepository permissaoRepository;

	@Transacional
	public Usuario salvar(Usuario usuario) {
		return this.usuarioRepository.salvar(usuario);
	}

	@Transacional
	public Usuario criarESalvarUsuario(Pessoa pessoa, TipoPermissao tipoPermissao) {
		Usuario novoUsuario = null;
		synchronized (semaforo) {
			novoUsuario = criarNovoUsuario(pessoa, tipoPermissao);
			novoUsuario = this.usuarioRepository.salvar(novoUsuario);
		}
		return novoUsuario;
	}

	private Usuario criarNovoUsuario(Pessoa pessoa, TipoPermissao tipoPermissao) {
		Usuario usuario = new Usuario();
		usuario.setMatricula(this.gerarNovaMatricula());
		usuario.setDtMatricula(LocalDate.now());
		usuario.setSenha(criptografarSenha("12345"));
		usuario.adicionarPermissao(getPermissao(tipoPermissao));
		usuario.setPessoa(pessoa);
		return usuario;
	}

	private String gerarNovaMatricula() {
		String ultimaMatriculaCadastrada = this.usuarioRepository.ultimaMatricula();
		return gerarNovaMatricula(ultimaMatriculaCadastrada);
	}

	public Permissao getPermissao(TipoPermissao tipoPermissao) {
		Permissao permissao = this.permissaoRepository.buscar("FROM Permissao AS p WHERE p.descricao = ?0",
				tipoPermissao);
		return permissao;
	}

	private String gerarNovaMatricula(String ultimaMatricula) {
		String parteLetras = ultimaMatricula.substring(0, 3);
		Integer parteNumeros = Integer.parseInt(ultimaMatricula.substring(3));
		parteNumeros = parteNumeros + 1;
		if (parteNumeros > 9999) {

			parteNumeros = 0;
			char[] cs = parteLetras.toCharArray();

			if (parteLetras.charAt(2) >= 65 && parteLetras.charAt(2) < 90) {
				cs[2] = (char) (parteLetras.charAt(2) + 1);
			} else if (parteLetras.charAt(1) >= 65 && parteLetras.charAt(1) < 90) {
				cs[1] = (char) (parteLetras.charAt(1) + 1);
				cs[2] = 65;
			} else if (parteLetras.charAt(0) >= 65 && parteLetras.charAt(0) < 90) {
				cs[0] = (char) (parteLetras.charAt(0) + 1);
				cs[1] = 65;
			}

			parteLetras = toString(cs);
		}

		String numerosDaMatriculaComoString = StringUtils.leftPad(String.valueOf(parteNumeros), 4, "0");

		String proximaMatricula = parteLetras + numerosDaMatriculaComoString;

		return proximaMatricula;
	}

	private String toString(char[] cs) {
		StringBuilder sb = new StringBuilder();
		for (char c : cs) {
			sb.append(c);
		}
		sb.trimToSize();
		return sb.toString();
	}

	public String criptografarSenha(String senha) {
		return new BCryptPasswordEncoder().encode(senha);
	}

	public Boolean senhasIguais(CharSequence rawPassword, String encodedPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
	}

	public static void main(String[] args) throws InterruptedException {
		UsuarioService usuariosService = new UsuarioService();
		String matricula = "AAA0000";
		System.out.println(matricula);
		do {
			matricula = usuariosService.gerarNovaMatricula(matricula);
			System.out.println(matricula);
		} while (matricula.compareTo("AAA0000") != 0);

	}

}
