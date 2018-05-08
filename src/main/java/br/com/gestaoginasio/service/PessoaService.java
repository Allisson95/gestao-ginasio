package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.auth.principal.Principal;
import br.com.gestaoginasio.model.Permissao;
import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.TipoPermissao;
import br.com.gestaoginasio.repository.PessoaRepository;
import br.com.gestaoginasio.util.Transacional;

public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private Principal principal;

	@Transacional
	public Pessoa salvar(Pessoa pessoa) {
		return this.pessoaRepository.salvar(pessoa);
	}

	public Pessoa buscarPeloCodigo(Long codigo) {
		return this.pessoaRepository.buscar(codigo);
	}

	public List<Pessoa> buscarTodosAdministradores() {
		Permissao permissao = usuarioService.getPermissao(TipoPermissao.ADMIN);
		return this.pessoaRepository.buscarTodos(
				"SELECT p FROM Pessoa AS p WHERE p.codigo != ?0 AND ?1 MEMBER OF p.usuario.permissoes",
				principal.getCodigo(), permissao);
	}

}
