package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Novidade;
import br.com.gestaoginasio.repository.NovidadeRepository;
import br.com.gestaoginasio.util.Transacional;

public class NovidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NovidadeRepository novidadeRepository;

	@Transacional
	public Novidade salvar(Novidade novidade) {
		return this.novidadeRepository.salvar(novidade);
	}

	public List<Novidade> buscarTodas() {
		return this.novidadeRepository.buscarTodos();
	}

	public Novidade buscarPeloCodigo(Long codigo) {
		return this.novidadeRepository.buscar(codigo);
	}

}
