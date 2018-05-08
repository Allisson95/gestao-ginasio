package br.com.gestaoginasio.controller.pessoa.professor;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.auth.principal.Principal;
import br.com.gestaoginasio.model.Professor;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.ProfessorService;

@Named
@ViewScoped
public class MinhasTurmasProfessorController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Principal principal;
	@Inject
	private ProfessorService professorService;

	private List<Turma> turmas;

	@PostConstruct
	public void abrirPagina() {
		Professor professor = this.professorService.buscarPeloCodigo(this.principal.getCodigo());
		this.turmas = this.professorService.buscarTurmasMinistradasPeloProfessor(professor);
	}

	public String realizarChamadaDaTurma(Turma turma) {
		return "RealizarChamadaDaTurma.jsf?faces-redirect=true&codigo=" + turma.getCodigo();
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

}
