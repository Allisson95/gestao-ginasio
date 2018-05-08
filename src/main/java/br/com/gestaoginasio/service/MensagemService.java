package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Mensagem;
import br.com.gestaoginasio.repository.MensagemRepository;
import br.com.gestaoginasio.util.Transacional;

public class MensagemService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MensagemRepository mensagemRepository;

	public Mensagem buscarPeloCodigo(Long codigo) {
		return this.mensagemRepository.buscar(codigo);
	}

	public List<Mensagem> buscarTodas() {
		return this.mensagemRepository.buscarTodos();
	}

	@Transacional
	public Mensagem salvar(Mensagem edicaoMensagem) {
		return this.mensagemRepository.salvar(edicaoMensagem);
	}

}
