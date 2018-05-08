package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gestaoginasio.model.Aula;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.AulaRepository;

public class AulaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AulaRepository aulaRepository;

	public Aula buscarPrimeiraAula(Turma turma) {
		Aula primeiraAulaDaTurma = this.aulaRepository.buscar(
				"SELECT a FROM Aula AS a WHERE a.turma = ?0 AND a.dtAula = (SELECT min(a2.dtAula) FROM Aula AS a2 WHERE a2.turma = ?1)",
				turma, turma);
		return primeiraAulaDaTurma;
	}

	public List<Aula> buscarAulasDaTurma(Turma turmaSelecionada) {
		return this.aulaRepository.buscarTodos("SELECT a FROM Aula AS a WHERE a.turma = ?0 ORDER BY a.dtAula",
				turmaSelecionada);
	}

}
