package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.model.Professor;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.ProfessorRepository;
import br.com.gestaoginasio.repository.TurmaRepository;
import br.com.gestaoginasio.util.Transacional;

public class ProfessorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProfessorRepository professorRepository;
	@Inject
	private TurmaRepository turmaRepository;

	public List<Professor> buscarTodos() {
		return this.professorRepository.buscarTodos();
	}

	@Transacional
	public Professor salvar(Professor professor) {
		return this.professorRepository.salvar(professor);
	}

	public Professor buscarPeloCodigo(Long codigo) {
		return this.professorRepository.buscar(codigo);
	}

	public List<Professor> buscarProfessoresDaModalidade(Modalidade modalidade) {
		return this.professorRepository.buscarTodos("SELECT p FROM Professor AS p WHERE ?0 MEMBER OF p.modalidades",
				modalidade);
	}

	public List<Turma> buscarTurmasMinistradasPeloProfessor(Professor professor) {
		return this.turmaRepository.buscarTodos("SELECT t FROM Turma AS t WHERE ?0 MEMBER OF t.professores", professor);
	}

}
