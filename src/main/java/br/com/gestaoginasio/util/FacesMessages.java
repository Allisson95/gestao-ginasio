package br.com.gestaoginasio.util;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@Named
@ApplicationScoped
public class FacesMessages implements Serializable {

	private static final long serialVersionUID = 1L;

	private String clientId;

	public FacesMessages() {
	}

	private void add(String titulo, String message, Severity severity) {
		FacesMessage msg = new FacesMessage();
		msg.setSeverity(severity);
		if (titulo != null && !titulo.trim().equals("")) {
			msg.setSummary(titulo);
			msg.setDetail(message);
		} else {
			msg.setSummary(message);
		}

		facesContext().addMessage((StringUtils.isNotBlank(this.clientId) ? this.clientId : null), msg);
	}

	public void error(String message) {
		error(null, message);
	}

	public void error(String titulo, String message) {
		add(titulo, message, FacesMessage.SEVERITY_ERROR);
	}

	public void fatal(String message) {
		fatal(null, message);
	}

	public void fatal(String titulo, String message) {
		add(titulo, message, FacesMessage.SEVERITY_FATAL);
	}

	public void info(String message) {
		info(null, message);
	}

	public void info(String titulo, String message) {
		add(titulo, message, FacesMessage.SEVERITY_INFO);
	}

	public void warn(String message) {
		warn(null, message);
	}

	public void warn(String titulo, String message) {
		add(titulo, message, FacesMessage.SEVERITY_WARN);
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void keepMessagesOnRedirect() {
		facesContext().getExternalContext().getFlash().setKeepMessages(true);
	}

	private FacesContext facesContext() {
		return FacesContext.getCurrentInstance();
	}
}