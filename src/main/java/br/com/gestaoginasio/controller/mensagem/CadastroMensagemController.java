package br.com.gestaoginasio.controller.mensagem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.model.Mensagem;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.MensagemService;
import br.com.gestaoginasio.service.TurmaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroMensagemController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private MensagemService mensagemService;
	@Inject
	private TurmaService turmaService;

	private List<Turma> turmas;

	private Mensagem edicaoMensagem;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Long codigo = Long.parseLong(parameterMap.get("codigo"));
			this.edicaoMensagem = mensagemService.buscarPeloCodigo(codigo);
			this.cadastrando = Boolean.FALSE;
			this.turmas = this.turmaService.buscarTodas();
		} else {
			this.edicaoMensagem = new Mensagem();
			this.turmas = this.turmaService.buscarTurmasAtivas();
		}
	}

	public String salvar() {
		if (this.cadastrando) {
			this.edicaoMensagem.setDtPostagem(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		}
		this.mensagemService.salvar(this.edicaoMensagem);

		this.facesMessages.info(this.messagesBundle.getMessage("mensagem.salvar"));
		this.facesMessages.keepMessagesOnRedirect();

		return "Mensagens.jsf?faces-redirect=true";
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public Mensagem getEdicaoMensagem() {
		return edicaoMensagem;
	}

	public void setEdicaoMensagem(Mensagem edicaoMensagem) {
		this.edicaoMensagem = edicaoMensagem;
	}

	public Boolean getCadastrando() {
		return cadastrando;
	}

}
