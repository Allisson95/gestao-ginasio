package br.com.gestaoginasio.controller.turma;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.TurmaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class TurmaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private TurmaService turmaService;

	private List<Turma> turmas;
	private List<Turma> turmasFiltradas;
	private Turma turmaSelecionada;

	@PostConstruct
	public void abrirPagina() {
		buscarTodasTurmas();
	}

	private void buscarTodasTurmas() {
		this.turmas = this.turmaService.buscarTodas();
	}

	public String prepararNovaTurma() {
		return "CadastrarTurma.jsf?faces-redirect=true";
	}

	public String editarTurma() {
		return "CadastrarTurma.jsf?faces-redirect=true&codigo=" + this.turmaSelecionada.getCodigo();
	}

	public String matricularAlunoNaTurma() {
		return "MatricularAluno.jsf?faces-redirect=true&codigo=" + this.turmaSelecionada.getCodigo();
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<Turma> getTurmasFiltradas() {
		return turmasFiltradas;
	}

	public void setTurmasFiltradas(List<Turma> turmasFiltradas) {
		this.turmasFiltradas = turmasFiltradas;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}

	public void alterarPropriedadeAtivo(Turma turma) {
		this.turmaService.alterarPropriedadeAtivo(turma);

		this.facesMessages.info(this.messagesBundle.getMessage("turma.ativar-desativar",
				(turma.getAtivo() ? "ativada" : "desativada")));

		this.buscarTodasTurmas();
		PrimeFaces.current().ajax().update(Arrays.asList("form:messages-dialog", "form:turmas-table"));
	}

}
