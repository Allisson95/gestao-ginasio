package br.com.gestaoginasio.controller.turma;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.gestaoginasio.exception.GestaoGinasioException;
import br.com.gestaoginasio.model.Aula;
import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.model.Professor;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.AulaService;
import br.com.gestaoginasio.service.ModalidadeService;
import br.com.gestaoginasio.service.ProfessorService;
import br.com.gestaoginasio.service.TurmaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroTurmaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private TurmaService turmaService;
	@Inject
	private AulaService aulaService;
	@Inject
	private ModalidadeService modalidadeService;
	@Inject
	private ProfessorService professorService;

	private Turma edicaoTurma;
	private LocalTime hrInicioDaAula;
	private LocalTime hrFimDaAula;

	private List<Modalidade> modalidades;
	private List<Professor> professores;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Short codigo = Short.parseShort(parameterMap.get("codigo"));
			this.edicaoTurma = this.turmaService.buscarPeloCodigo(codigo);
			Aula primeiraAulaDaTurma = this.aulaService.buscarPrimeiraAula(this.edicaoTurma);
			this.hrInicioDaAula = primeiraAulaDaTurma.getHrInicio();
			this.hrFimDaAula = primeiraAulaDaTurma.getHrFim();
			buscarProfessoresDaModalidade();
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoTurma = new Turma();
		}
		buscarModalidadesAtivas();
	}

	public void buscarModalidadesAtivas() {
		this.modalidades = this.modalidadeService.buscarTodas();
	}

	public void buscarProfessoresDaModalidade() {
		this.professores = this.professorService.buscarProfessoresDaModalidade(this.edicaoTurma.getModalidade());
	}

	public String salvar() {
		try {
			this.turmaService.salvar(this.edicaoTurma, this.cadastrando, this.hrInicioDaAula, this.hrFimDaAula);
		} catch (GestaoGinasioException e) {
			this.facesMessages.warn(e.getMessage());
			return "";
		}
		this.facesMessages.info(this.messagesBundle.getMessage("turma.salvar"));
		this.facesMessages.keepMessagesOnRedirect();
		return "Turmas.jsf?faces-redirect=true";
	}

	public Turma getEdicaoTurma() {
		return edicaoTurma;
	}

	public void setEdicaoTurma(Turma edicaoTurma) {
		this.edicaoTurma = edicaoTurma;
	}

	public LocalTime getHrInicioDaAula() {
		return hrInicioDaAula;
	}

	public void setHrInicioDaAula(LocalTime hrInicioDaAula) {
		this.hrInicioDaAula = hrInicioDaAula;
	}

	public LocalTime getHrFimDaAula() {
		return hrFimDaAula;
	}

	public void setHrFimDaAula(LocalTime hrFimDaAula) {
		this.hrFimDaAula = hrFimDaAula;
	}

	public List<Modalidade> getModalidades() {
		return modalidades;
	}

	public List<Professor> getProfessores() {
		return professores;
	}

	public Boolean getCadastrando() {
		return cadastrando;
	}

	public String abreviarNome(String nomeCompleto) {

		if (StringUtils.isBlank(nomeCompleto)) {
			throw new RuntimeException("Nome não poder ser nulo ou vazio.");
		}

		String[] partesDoNome = nomeCompleto.split("\\s");

		StringBuilder nomeAbreviado = new StringBuilder();
		int quantidadeDeNomes = partesDoNome.length;
		for (int i = 0; i < quantidadeDeNomes; i++) {
			String parteDoNome = partesDoNome[i];
			if (i == 0 || i == (quantidadeDeNomes - 1)) {
				nomeAbreviado.append(parteDoNome);
			} else {
				nomeAbreviado.append(parteDoNome.charAt(0)).append(".");
			}
			nomeAbreviado.append(" ");
		}

		nomeAbreviado.trimToSize();
		return nomeAbreviado.toString();
	}

}
