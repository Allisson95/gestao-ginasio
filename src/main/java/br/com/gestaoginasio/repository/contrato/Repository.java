package br.com.gestaoginasio.repository.contrato;

import java.io.Serializable;
import java.util.List;

public interface Repository<T extends Serializable, ID> {

	public T buscar(ID id);

	public T buscar(String sqlQuery, Object... parametros);

	public List<T> buscarTodos();

	public List<T> buscarTodos(String sqlQuery, Object... parametros);

	public Long contar();

	public void deletar(T entidade);

	public void deletar(List<T> entidades);

	public T salvar(T entidade);

	public List<T> salvar(List<T> entidades);

}
