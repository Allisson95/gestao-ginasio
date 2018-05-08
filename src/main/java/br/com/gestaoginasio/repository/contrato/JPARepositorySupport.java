package br.com.gestaoginasio.repository.contrato;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Named
@Transactional(value = TxType.MANDATORY)
public abstract class JPARepositorySupport {

	@PersistenceContext
	@Inject
	protected EntityManager entityManager;

	public JPARepositorySupport() {
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

}
