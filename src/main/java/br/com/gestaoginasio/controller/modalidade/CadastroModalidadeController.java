package br.com.gestaoginasio.controller.modalidade;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.service.ModalidadeService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroModalidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private ModalidadeService modalidadeService;

	private Modalidade edicaoModalidade;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Short codigo = Short.parseShort(parameterMap.get("codigo"));
			this.edicaoModalidade = this.modalidadeService.buscarPeloCodigo(codigo);
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoModalidade = new Modalidade();
		}
	}

	public String salvar() {
		Modalidade modalidadeSalva = this.modalidadeService.salvar(this.edicaoModalidade);

		this.facesMessages.info(this.messagesBundle.getMessage("modalidade.salvar", modalidadeSalva.getNome()));
		this.facesMessages.keepMessagesOnRedirect();
		return "Modalidades.jsf?faces-redirect=true";
	}

	public Modalidade getEdicaoModalidade() {
		return edicaoModalidade;
	}

	public void setEdicaoModalidade(Modalidade edicaoModalidade) {
		this.edicaoModalidade = edicaoModalidade;
	}

	public Boolean getCadastrando() {
		return cadastrando;
	}

}
