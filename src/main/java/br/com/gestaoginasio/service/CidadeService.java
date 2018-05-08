package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Cidade;
import br.com.gestaoginasio.model.Estado;
import br.com.gestaoginasio.repository.CidadeRepository;

public class CidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CidadeRepository cidadeRepository;

	public Cidade buscarPorId(Short id) {
		return this.cidadeRepository.buscar(id);
	}

	public List<Cidade> buscarCidadesPeloEstado(Estado estado) {
		return this.cidadeRepository.buscarTodos("FROM Cidade AS c WHERE c.estado = ?0 ORDER BY c.cidade", estado);
	}

	public Cidade buscarPorNomeEUf(String cidade, Estado estado) {
		return this.cidadeRepository.buscar("FROM Cidade AS c WHERE c.cidade LIKE ?0 AND c.estado = ?1",
				String.format("%%%s%%", cidade), estado);
	}
}
