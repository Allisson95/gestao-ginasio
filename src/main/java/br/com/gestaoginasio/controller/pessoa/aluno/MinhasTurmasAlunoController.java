package br.com.gestaoginasio.controller.pessoa.aluno;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.auth.principal.Principal;
import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Frequencia;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.AlunoService;
import br.com.gestaoginasio.service.FrequenciaService;

@Named
@ViewScoped
public class MinhasTurmasAlunoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Principal principal;
	@Inject
	private AlunoService alunoService;
	@Inject
	private FrequenciaService frequenciaService;

	private Aluno aluno;

	private List<Turma> turmas;
	private List<DesempenhoGeralDoAluno> desempenhoGeralDoAluno = new ArrayList<>(0);

	@PostConstruct
	public void abrirPagina() {
		this.aluno = this.alunoService.buscarPeloCodigo(this.principal.getCodigo());
		this.turmas = this.alunoService.buscarTurmasFrequentadaPeloAluno(this.aluno);
		if (!this.turmas.isEmpty()) {
			this.turmas.forEach(t -> {
				buscarFrequenciaDasAulasMinistradasNaTurma(t);
			});
		}
	}

	private void buscarFrequenciaDasAulasMinistradasNaTurma(Turma turma) {
		List<Frequencia> frequenciaDoAluno = this.frequenciaService.buscarPeloAlunoETurma(aluno, turma);

		Integer totalDeFaltas = Integer.valueOf(0);

		Integer totalDePontos = Integer.valueOf(0);
		BigDecimal desempenhoMedio = BigDecimal.ZERO;

		for (Frequencia frequencia : frequenciaDoAluno) {
			if (frequencia.faltou()) {
				totalDeFaltas += 1;
			}
			totalDePontos += Optional.ofNullable(frequencia.getAvaliacao()).orElse(0);
		}

		try {
			desempenhoMedio = BigDecimal.valueOf((((double) totalDePontos) / frequenciaDoAluno.size())).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
		}
		DesempenhoGeralDoAluno dga = new DesempenhoGeralDoAluno(turma, totalDeFaltas, desempenhoMedio);
		this.desempenhoGeralDoAluno.add(dga);
	}

	public List<DesempenhoGeralDoAluno> getDesempenhoGeralDoAluno() {
		return desempenhoGeralDoAluno;
	}

	public class DesempenhoGeralDoAluno {

		private Turma turma;
		private Integer totalDeFaltas;
		private BigDecimal desempenhoMedio;

		public DesempenhoGeralDoAluno(Turma turma, Integer totalDeFaltas, BigDecimal desempenhoMedio) {
			this.turma = turma;
			this.totalDeFaltas = totalDeFaltas;
			this.desempenhoMedio = desempenhoMedio;
		}

		public Turma getTurma() {
			return turma;
		}

		public void setTurma(Turma turma) {
			this.turma = turma;
		}

		public Integer getTotalDeFaltas() {
			return totalDeFaltas;
		}

		public void setTotalDeFaltas(Integer totalDeFaltas) {
			this.totalDeFaltas = totalDeFaltas;
		}

		public BigDecimal getDesempenhoMedio() {
			return desempenhoMedio;
		}

		public void setDesempenhoMedio(BigDecimal desempenhoMedio) {
			this.desempenhoMedio = desempenhoMedio;
		}

	}
}
