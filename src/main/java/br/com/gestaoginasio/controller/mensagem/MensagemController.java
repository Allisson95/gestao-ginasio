package br.com.gestaoginasio.controller.mensagem;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.model.Mensagem;
import br.com.gestaoginasio.service.MensagemService;

@Named
@ViewScoped
public class MensagemController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MensagemService mensagemService;

	private Mensagem mensagemSelecionada;
	private List<Mensagem> mensagens;
	private List<Mensagem> mensagensFiltradas;

	@PostConstruct
	public void abrirPagina() {
		buscarTodasMensagens();
	}

	private void buscarTodasMensagens() {
		this.mensagens = this.mensagemService.buscarTodas();
	}

	public String prepararNovaMensagem() {
		return "CadastrarMensagem.jsf?faces-redirect=true";
	}

	public String editarMensagem() {
		return "CadastrarMensagem.jsf?faces-redirect=true&codigo=" + this.mensagemSelecionada.getCodigo();
	}

	public Mensagem getMensagemSelecionada() {
		return mensagemSelecionada;
	}

	public void setMensagemSelecionada(Mensagem mensagemSelecionada) {
		this.mensagemSelecionada = mensagemSelecionada;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public List<Mensagem> getMensagensFiltradas() {
		return mensagensFiltradas;
	}

	public void setMensagensFiltradas(List<Mensagem> mensagensFiltradas) {
		this.mensagensFiltradas = mensagensFiltradas;
	}

}
