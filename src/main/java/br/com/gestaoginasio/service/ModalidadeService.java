package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.repository.ModalidadeRepository;
import br.com.gestaoginasio.util.Transacional;

public class ModalidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ModalidadeRepository modalidadeRepository;

	@Transacional
	public Modalidade salvar(Modalidade modalidade) {
		return this.modalidadeRepository.salvar(modalidade);
	}

	public Modalidade buscarPeloCodigo(Short codigo) {
		return this.modalidadeRepository.buscar(codigo);
	}

	public List<Modalidade> buscarTodas() {
		return this.modalidadeRepository.buscarTodos();
	}

	public List<Modalidade> buscarTodasAtivas() {
		return this.modalidadeRepository.buscarTodos("FROM Modalidade AS m WHERE m.ativo IS TRUE");
	}

}
