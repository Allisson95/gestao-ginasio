package br.com.gestaoginasio.repository;

import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.contrato.RepositoryImpl;

public class TurmaRepository extends RepositoryImpl<Turma, Short> {

	public void alterarPropriedadeAtivo(Turma turma) {
		super.entityManager.createQuery("UPDATE Turma SET ativo = ?0 WHERE codigo = ?1")
				.setParameter(0, turma.getAtivo()).setParameter(1, turma.getCodigo()).executeUpdate();
	}

}
