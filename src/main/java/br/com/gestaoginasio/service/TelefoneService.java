package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.Telefone;
import br.com.gestaoginasio.repository.TelefoneRepository;
import br.com.gestaoginasio.util.Transacional;

public class TelefoneService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TelefoneRepository telefoneRepository;

	public List<Telefone> buscarContatoDaPessoa(Long codigo) {
		List<Telefone> telefone = this.telefoneRepository
				.buscarTodos("SELECT t FROM Telefone AS t WHERE t.pessoa.codigo = ?0", codigo);
		return Optional.ofNullable(telefone).orElse(new ArrayList<>(0));
	}

	@Transacional
	public void apagar(Telefone telefone) {
		this.telefoneRepository.deletar(telefone);
	}

	@Transacional
	public List<Telefone> salvarTodos(List<Telefone> telefones, Pessoa pessoa) {
		if (telefones != null && !telefones.isEmpty()) {
			telefones.forEach(t -> {
				if (t.getPessoa() == null) {
					t.setPessoa(pessoa);
				}
			});
			return this.telefoneRepository.salvar(telefones);
		} else {
			return new ArrayList<>(0);
		}
	}

	@Transacional
	public void remover(Telefone telefone) {
		this.telefoneRepository.deletar(telefone);
	}

	@Transacional
	public void removerTodos(List<Telefone> telefonesRemovidos) {
		telefonesRemovidos.forEach(t -> {
			if (t.getCodigo() != null) {
				this.telefoneRepository.deletar(t);
			}
		});
	}

}
