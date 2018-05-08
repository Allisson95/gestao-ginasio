package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Estado;
import br.com.gestaoginasio.repository.EstadoRepository;

public class EstadoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EstadoRepository estadoRepository;

	public Estado buscarPorId(Byte id) {
		return this.estadoRepository.buscar(id);
	}

	public List<Estado> buscarTodos() {
		return this.estadoRepository.buscarTodos("FROM Estado AS e ORDER BY e.sigla");
	}

	public Estado buscarPelaSigla(String sigla) {
		return this.estadoRepository.buscar("FROM Estado AS e WHERE e.sigla = ?0", sigla);
	}
}
