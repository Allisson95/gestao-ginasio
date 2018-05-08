package br.com.gestaoginasio.repository.contrato;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Named
@Transactional(value = TxType.MANDATORY)
public abstract class RepositoryImpl<T extends Serializable, ID> extends JPARepositorySupport
		implements Repository<T, ID> {

	private final Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public RepositoryImpl() {
		this.entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
	}

	public CriteriaQuery<T> getCriteriaQuery() {
		return this.getCriteriaBuilder().createQuery(entityClass);
	}

	@Override
	public T buscar(ID id) {
		return this.entityManager.find(entityClass, id);
	}

	@Override
	public T buscar(String sqlQuery, Object... parametros) {
		TypedQuery<T> typedQuery = this.entityManager.createQuery(sqlQuery, entityClass);
		if (parametros != null) {
			for (int i = 0; i < parametros.length; i++) {
				Object parametro = parametros[i];
				typedQuery.setParameter(i, parametro);
			}
		}
		return typedQuery.getSingleResult();
	}

	@Override
	public List<T> buscarTodos() {
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery();
		criteriaQuery.from(entityClass);
		return this.entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<T> buscarTodos(String sqlQuery, Object... parametros) {
		TypedQuery<T> typedQuery = this.entityManager.createQuery(sqlQuery, entityClass);
		if (parametros != null) {
			for (int i = 0; i < parametros.length; i++) {
				Object parametro = parametros[i];
				typedQuery.setParameter(i, parametro);
			}
		}
		return typedQuery.getResultList();
	}

	@Override
	public Long contar() {
		CriteriaQuery<Long> criteriaQuery = getCriteriaBuilder().createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(getCriteriaBuilder().count(root));
		return this.entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	@Override
	public void deletar(T entidade) {
		T merge = this.entityManager.merge(entidade);
		this.entityManager.remove(merge);
	}

	@Override
	public void deletar(List<T> entidades) {
		entidades.forEach(e -> {
			T merge = this.entityManager.merge(e);
			this.entityManager.remove(merge);
		});
	}

	@Override
	public T salvar(T entidade) {
		return this.entityManager.merge(entidade);
	}

	@Override
	public List<T> salvar(List<T> entidades) {
		List<T> entidadesSalvas = new ArrayList<>();
		entidades.forEach(e -> {
			entidadesSalvas.add(this.entityManager.merge(e));
		});
		return entidadesSalvas;
	}

}
