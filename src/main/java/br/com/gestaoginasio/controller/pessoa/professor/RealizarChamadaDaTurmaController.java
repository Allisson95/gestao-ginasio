package br.com.gestaoginasio.controller.pessoa.professor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.AlunoFrequencia;
import br.com.gestaoginasio.model.Aula;
import br.com.gestaoginasio.model.Frequencia;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.AlunoService;
import br.com.gestaoginasio.service.AulaService;
import br.com.gestaoginasio.service.FrequenciaService;
import br.com.gestaoginasio.service.TurmaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class RealizarChamadaDaTurmaController implements Serializable {

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
	private AlunoService alunoService;
	@Inject
	private FrequenciaService frequenciaService;

	List<Aula> aulasDaTurma;

	private Turma turmaSelecionada;
	private Aula aulaSelecionada;

	private List<Aluno> alunosDaTurma;
	private List<AlunoFrequencia> alunosFrequencias;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Short codigo = Short.parseShort(parameterMap.get("codigo"));
			this.turmaSelecionada = this.turmaService.buscarPeloCodigo(codigo);
			this.buscarAulasDaTurma();
			this.buscarAlunosDaTurma();
		}

	}

	private void buscarAulasDaTurma() {
		this.aulasDaTurma = this.aulaService.buscarAulasDaTurma(this.turmaSelecionada);
	}

	private void buscarAlunosDaTurma() {
		this.alunosDaTurma = this.alunoService.buscarAlunosDaTurma(this.turmaSelecionada);
	}

	public void buscarFrequenciaDosAlunos() {
		alunosFrequencias = new ArrayList<>();
		this.alunosDaTurma.forEach(aluno -> {
			AlunoFrequencia alunoFrequencia = null;
			Frequencia frequencia = null;
			try {
				frequencia = this.frequenciaService.buscarPeloAlunoTurmaEAula(aluno, turmaSelecionada, aulaSelecionada);
				alunoFrequencia = new AlunoFrequencia(aluno, frequencia);
			} catch (NoResultException e) {
				frequencia = new Frequencia(aluno, turmaSelecionada, aulaSelecionada);
				alunoFrequencia = new AlunoFrequencia(aluno, frequencia);
			}

			alunosFrequencias.add(alunoFrequencia);
		});
	}

	public void salvar() {
		this.alunosFrequencias.forEach(af -> {
			this.frequenciaService.salvar(af.getFrequencia());
		});

		this.facesMessages.info(this.messagesBundle.getMessage("professor.salvar-frequencia"));
	}

	public List<Aula> getAulasDaTurma() {
		return aulasDaTurma;
	}

	public Aula getAulaSelecionada() {
		return aulaSelecionada;
	}

	public void setAulaSelecionada(Aula aulaSelecionada) {
		this.aulaSelecionada = aulaSelecionada;
	}

	public List<AlunoFrequencia> getAlunosFrequencias() {
		return alunosFrequencias;
	}

	public void setAlunosFrequencias(List<AlunoFrequencia> alunosFrequencias) {
		this.alunosFrequencias = alunosFrequencias;
	}

	public String criarLabelSelectAula(Aula aula) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dtAula = aula.getDtAula();
		return dtf.format(dtAula);
	}
}
