package br.com.gestaoginasio.util;

import java.io.Serializable;

public interface MessagesBundle extends Serializable {

	public String getMessage(String messageKey);

	public String getMessage(String messageKey, Object... params);

}
