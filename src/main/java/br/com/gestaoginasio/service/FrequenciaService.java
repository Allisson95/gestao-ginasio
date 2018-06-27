package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Aula;
import br.com.gestaoginasio.model.Frequencia;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.FrequenciaRepository;
import br.com.gestaoginasio.util.Transacional;

public class FrequenciaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FrequenciaRepository frequenciaRepository;

	public Frequencia buscarPeloAlunoTurmaEAula(Aluno aluno, Turma turma, Aula aula) throws NoResultException {
		return this.frequenciaRepository.buscar(
				"SELECT f FROM Frequencia AS f WHERE f.codigoAlunoMatriculaTurma = ?0 AND f.codigoTurmaMatriculaTurma = ?1 AND f.aula = ?2",
				aluno.getCodigo(), turma.getCodigo(), aula);
	}

	@Transacional
	public Frequencia salvar(Frequencia frequencia) {
		return this.frequenciaRepository.salvar(frequencia);
	}

	public List<Frequencia> buscarPeloAlunoETurma(Aluno aluno, Turma turma) {
		return this.frequenciaRepository.buscarTodos(
				"SELECT f FROM Frequencia AS f WHERE f.codigoAlunoMatriculaTurma = ?0 AND f.codigoTurmaMatriculaTurma = ?1",
				aluno.getCodigo(), turma.getCodigo());
	}

}
