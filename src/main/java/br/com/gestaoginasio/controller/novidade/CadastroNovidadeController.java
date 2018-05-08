package br.com.gestaoginasio.controller.novidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.auth.principal.Principal;
import br.com.gestaoginasio.model.Novidade;
import br.com.gestaoginasio.service.NovidadeService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroNovidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Principal principal;
	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private NovidadeService novidadeService;

	private Novidade edicaoNovidade;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			this.edicaoNovidade = novidadeService.buscarPeloCodigo(Long.parseLong(parameterMap.get("codigo")));
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoNovidade = new Novidade();
		}
	}

	public String salvar() {
		if (this.cadastrando) {
			this.edicaoNovidade.setDtPostagem(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
			this.edicaoNovidade.setPessoa(this.principal.getUsuario().getPessoa());
		}
		this.novidadeService.salvar(this.edicaoNovidade);

		this.facesMessages.info(this.messagesBundle.getMessage("novidade.salvar"));
		this.facesMessages.keepMessagesOnRedirect();

		return "Novidades.jsf?faces-redirect=true";
	}

	public Novidade getEdicaoNovidade() {
		return edicaoNovidade;
	}

	public void setEdicaoNovidade(Novidade edicaoNovidade) {
		this.edicaoNovidade = edicaoNovidade;
	}

	public Boolean getCadastrando() {
		return cadastrando;
	}

}
