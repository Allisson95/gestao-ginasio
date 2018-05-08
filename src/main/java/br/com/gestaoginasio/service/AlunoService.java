package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.AlunoRepository;
import br.com.gestaoginasio.util.Transacional;

public class AlunoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AlunoRepository alunoRepository;

	public List<Aluno> buscarTodos() {
		return this.alunoRepository.buscarTodos();
	}

	@Transacional
	public Aluno salvar(Aluno aluno) {
		return this.alunoRepository.salvar(aluno);
	}

	public Aluno buscarPeloCodigo(Long codigo) {
		return this.alunoRepository.buscar(codigo);
	}

	public List<Aluno> buscarAlunosDaTurma(Turma turma) {
		return this.alunoRepository.buscarTodos("SELECT a FROM Aluno AS a WHERE ?0 MEMBER OF a.turmas ORDER BY a.nome",
				turma);
	}

}
