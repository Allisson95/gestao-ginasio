package br.com.gestaoginasio.converter.jsf;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import br.com.gestaoginasio.repository.contrato.Repository;
import br.com.gestaoginasio.util.FacesMessages;

@SuppressWarnings("unchecked")
public abstract class JsfGenericConverter<T extends Serializable, ID> implements Converter<T> {

	private final String ERROR_MSG_TITULO = String.format("Erro ao inicializar o %s.", this.getClass().getSimpleName());
	private static final String PACKAGE_REPOSITORYS = "br.com.gestaoginasio.repository";

	private FacesMessages facesMessages;

	private final Class<T> entityClass;
	private final Class<ID> idClass;

	private final Repository<T, ID> repository;

	public JsfGenericConverter() {
		this.entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
		this.idClass = ((Class<ID>) ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[1]));

		String fullNameRepository = String.format("%s.%s%s", PACKAGE_REPOSITORYS, this.entityClass.getSimpleName(),
				"Repository");
		Class<?> undefinedRepository = null;
		try {
			undefinedRepository = Class.forName(fullNameRepository, false, this.getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			String corpo = "Repository não encontrado.";
			errorMessage(corpo);
			throw new RuntimeException(ERROR_MSG_TITULO + " " + corpo);
		}
		this.repository = (Repository<T, ID>) CDI.current().select(undefinedRepository).get();
	}

	@Override
	public T getAsObject(FacesContext context, UIComponent component, String value) {
		T entidade = null;
		if (StringUtils.isNotBlank(value)) {
			ID id = null;
			try {
				id = this.idClass.getConstructor(String.class).newInstance(value);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				String corpo = "Não foi encontrado um construtor para o ID que recebesse um String como parametro.";
				errorMessage(corpo);
				throw new RuntimeException(ERROR_MSG_TITULO + " " + corpo);
			}

			entidade = repository.buscar(id);
		}
		return entidade;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, T value) {
		String idReturn = null;
		if (value != null) {
			T entidade = this.entityClass.cast(value);
			ID codigo = null;
			try {
				codigo = (ID) entidade.getClass().getMethod("getCodigo").invoke(entidade);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				String corpo = "Não foi encontrado um construtor para o ID que recebesse um String como parametro.";
				errorMessage(corpo);
				throw new RuntimeException(ERROR_MSG_TITULO + " " + corpo);
			}

			idReturn = ((codigo != null) ? String.valueOf(codigo) : null);
		}
		return idReturn;
	}

	private void errorMessage(String corpo) {
		if (this.facesMessages == null) {
			this.facesMessages = CDI.current().select(FacesMessages.class).get();
		}
		this.facesMessages.error(ERROR_MSG_TITULO, corpo);
	}

}
