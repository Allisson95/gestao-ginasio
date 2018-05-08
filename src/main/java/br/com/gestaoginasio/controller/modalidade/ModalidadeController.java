package br.com.gestaoginasio.controller.modalidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.service.ModalidadeService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class ModalidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private ModalidadeService modalidadeService;

	private List<Modalidade> modalidades;
	private List<Modalidade> modalidadesFiltradas;

	private Modalidade modalidadeSelecionada;

	@PostConstruct
	public void abrirPagina() {
		this.buscarTodasModalidades();
	}

	private void buscarTodasModalidades() {
		this.modalidades = this.modalidadeService.buscarTodas();
	}

	public String prepararNovaModalidade() {
		return "CadastrarModalidade.jsf?faces-redirect=true";
	}

	public String editarModalidade() {
		return "CadastrarModalidade.jsf?faces-redirect=true&codigo=" + this.modalidadeSelecionada.getCodigo();
	}

	public void alterarPropriedadeAtivo(Modalidade modalidade) {
		Modalidade modalidadeSalva = this.modalidadeService.salvar(modalidade);

		this.facesMessages.info(this.messagesBundle.getMessage("modalidade.ativar-desativar", modalidadeSalva.getNome(),
				(modalidadeSalva.getAtivo() ? "ativada" : "desativada")));

		this.buscarTodasModalidades();
		PrimeFaces.current().ajax().update(Arrays.asList("form:messages-dialog", "form:modalidades-table"));
	}

	public List<Modalidade> getModalidades() {
		return modalidades;
	}

	public void setModalidades(List<Modalidade> modalidades) {
		this.modalidades = modalidades;
	}

	public List<Modalidade> getModalidadesFiltradas() {
		return modalidadesFiltradas;
	}

	public void setModalidadesFiltradas(List<Modalidade> modalidadesFiltradas) {
		this.modalidadesFiltradas = modalidadesFiltradas;
	}

	public Modalidade getModalidadeSelecionada() {
		return modalidadeSelecionada;
	}

	public void setModalidadeSelecionada(Modalidade modalidadeSelecionada) {
		this.modalidadeSelecionada = modalidadeSelecionada;
	}

}
