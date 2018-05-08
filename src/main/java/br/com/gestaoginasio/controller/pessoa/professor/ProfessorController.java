package br.com.gestaoginasio.controller.pessoa.professor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.gestaoginasio.model.Professor;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.service.ProfessorService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class ProfessorController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private ProfessorService professorService;

	private List<Professor> professores;
	private List<Professor> professoresFiltrados;
	private Professor professorSelecionado;

	@PostConstruct
	public void abrirPagina() {
		buscarTodosProfessores();
	}

	private void buscarTodosProfessores() {
		this.professores = this.professorService.buscarTodos();
	}

	public String prepararNovoProfessor() {
		return "CadastrarProfessor.jsf?faces-redirect=true";
	}

	public String editarProfessor() {
		return "CadastrarProfessor.jsf?faces-redirect=true&codigo=" + this.professorSelecionado.getCodigo();
	}

	public void alterarPropriedadeAtivo(Professor professor) {
		Professor professorSalvo = this.professorService.salvar(professor);

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.ativar-desativar", professorSalvo.getNome(),
				(professorSalvo.getAtivo() ? "ativado" : "desativado")));

		this.buscarTodosProfessores();
		PrimeFaces.current().ajax().update(Arrays.asList("form:messages-dialog", "form:professores-table"));
	}

	public List<Professor> getProfessores() {
		return professores;
	}

	public List<Professor> getProfessoresFiltrados() {
		return professoresFiltrados;
	}

	public void setProfessoresFiltrados(List<Professor> professoresFiltrados) {
		this.professoresFiltrados = professoresFiltrados;
	}

	public Professor getProfessorSelecionado() {
		return professorSelecionado;
	}

	public void setProfessorSelecionado(Professor professorSelecionado) {
		this.professorSelecionado = professorSelecionado;
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
