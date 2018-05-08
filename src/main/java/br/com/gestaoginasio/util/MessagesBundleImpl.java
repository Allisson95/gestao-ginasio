package br.com.gestaoginasio.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.inject.Named;

@Named
public class MessagesBundleImpl implements MessagesBundle {

	private static final long serialVersionUID = 1L;

	private ResourceBundle resourceBundle;

	public MessagesBundleImpl() {
		loadMessagesProperties();
	}

	private void loadMessagesProperties() {
		this.resourceBundle = ResourceBundle.getBundle("gestaoginasio_messages", new Locale("pt", "BR"));
	}

	@Override
	public String getMessage(String messageKey) {
		return this.resourceBundle.getString(messageKey);
	}

	@Override
	public String getMessage(String messageKey, Object... params) {
		String messageWithParam = getMessage(messageKey);
		String message = MessageFormat.format(messageWithParam, params);
		return message;
	}

}
