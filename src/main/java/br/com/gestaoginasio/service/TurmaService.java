package br.com.gestaoginasio.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.gestaoginasio.exception.GestaoGinasioException;
import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Aula;
import br.com.gestaoginasio.model.DiasSemana;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.repository.TurmaRepository;
import br.com.gestaoginasio.util.MessagesBundle;
import br.com.gestaoginasio.util.Transacional;

public class TurmaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private TurmaRepository turmaRepository;

	public Turma buscarPeloCodigo(Short codigo) {
		return this.turmaRepository.buscar(codigo);
	}

	public Turma buscar(String sqlQuery, Object... parametros) {
		return this.turmaRepository.buscar(sqlQuery, parametros);
	}

	public List<Turma> buscarTodas() {
		return this.turmaRepository.buscarTodos();
	}

	public List<Turma> buscarTurmasAtivas() {
		return this.turmaRepository.buscarTodos("FROM Turma AS t WHERE t.ativo IS TRUE");
	}

	@Transacional
	public void alterarPropriedadeAtivo(Turma edicaoTurma) {
		this.turmaRepository.alterarPropriedadeAtivo(edicaoTurma);
	}

	@Transacional
	public Aluno matricularAlunoNaTurma(Aluno aluno, Turma turma) throws GestaoGinasioException {
		if (turma.getAlunos().contains(aluno)) {
			throw new GestaoGinasioException(
					this.messagesBundle.getMessage("turma.erro-matricula-duplicada", aluno.getNome()));
		}
		turma.adicionarAluno(aluno);
		salvarTurma(turma, Boolean.FALSE, null, null);
		return aluno;
	}

	@Transacional
	public Turma salvar(Turma edicaoTurma, Boolean cadastrando, LocalTime hrInicioDaAula, LocalTime hrFimDaAula) {
		return salvarTurma(edicaoTurma, cadastrando, hrInicioDaAula, hrFimDaAula);
	}

	private Turma salvarTurma(Turma edicaoTurma, Boolean cadastrando, LocalTime hrInicioDaAula, LocalTime hrFimDaAula)
			throws GestaoGinasioException {
		if (edicaoTurma.getProfessores() == null || edicaoTurma.getProfessores().isEmpty()) {
			throw new GestaoGinasioException(this.messagesBundle.getMessage("turma.salvar-professor-vazio"));
		}
		if (cadastrando) {
			if (edicaoTurma.getDtInicio().isBefore(LocalDate.now())) {
				throw new GestaoGinasioException(this.messagesBundle.getMessage("turma.salvar-data-retroativa"));
			}
			if (edicaoTurma.getDtFim().isBefore(edicaoTurma.getDtInicio())) {
				throw new GestaoGinasioException(this.messagesBundle.getMessage("turma.salvar-data-fim-antes-inicio"));
			}

			edicaoTurma.setAno(Year.of(edicaoTurma.getDtInicio().getYear()));
			String identificacao = gerarIdentificacaoTurma(edicaoTurma);
			edicaoTurma.setIdentificacao(identificacao);

			gerarAulasDaTurma(edicaoTurma, hrInicioDaAula, hrFimDaAula);
		}
		return this.turmaRepository.salvar(edicaoTurma);
	}

	private String gerarIdentificacaoTurma(Turma edicaoTurma) {
		LocalDate dataInicioDaTurma = edicaoTurma.getDtInicio();
		String mesInicioDaTurma = dataInicioDaTurma.getMonth()
				.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("pt-BR")).toUpperCase();
		int anoInicioDaTurma = edicaoTurma.getAno().getValue();
		String sigla = criarSigla(edicaoTurma.getModalidade().getNome());
		// Exemplo: MAR2016FUT
		String identificacao = String.format("%s%d%s", mesInicioDaTurma, anoInicioDaTurma, sigla);
		return identificacao;
	}

	private String criarSigla(String nomeCompleto) {
		StringBuilder sigla = new StringBuilder();

		if (nomeCompleto.contains(" ")) {
			for (String nome : nomeCompleto.split(" ")) {
				sigla.append(nome.substring(0, 1));
			}
		} else if (nomeCompleto.contains("-")) {
			for (String nome : nomeCompleto.split("-")) {
				sigla.append(nome.substring(0, 1));
			}
		} else {
			sigla.append(nomeCompleto.substring(0, 3));
		}

		if (sigla.length() < 3) {
			sigla.append(nomeCompleto.charAt(nomeCompleto.length() - 1));
		}

		return sigla.toString().toUpperCase();
	}

	private void gerarAulasDaTurma(Turma turma, LocalTime hrInicioDaAula, LocalTime hrFimDaAula)
			throws GestaoGinasioException {
		LocalDate dataDeInicio = turma.getDtInicio();
		LocalDate dataDeFim = turma.getDtFim();

		DiasSemana diasSemana = turma.getPeriodicidade().getDiasSemana();
		List<Method> getMethods = Arrays.asList(diasSemana.getClass().getDeclaredMethods()).stream()
				.filter(m -> m.getName().startsWith("get")).collect(Collectors.toList());

		List<DayOfWeek> diasDaSemanaSelecionados = new ArrayList<>();
		getMethods.forEach(m -> {
			Boolean bDiaDaSemana = Boolean.FALSE;
			try {
				bDiaDaSemana = (Boolean) m.invoke(diasSemana);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			if (bDiaDaSemana) {
				String nomeDoMetodo = m.getName().substring(3).toUpperCase();
				DayOfWeek dayOfWeek = getDayOfWeek(nomeDoMetodo);
				diasDaSemanaSelecionados.add(dayOfWeek);
			}
		});

		if (diasDaSemanaSelecionados.isEmpty()) {
			throw new GestaoGinasioException(this.messagesBundle.getMessage("turma.salvar-periodicidade-vazia"));
		}

		List<LocalDate> datasAuxiliares = new ArrayList<>();
		diasDaSemanaSelecionados.forEach(dw -> {
			LocalDate dataAux = dataDeInicio;
			while (dataAux.getDayOfWeek().compareTo(dw) != 0) {
				dataAux = dataAux.plusDays(1);
			}
			datasAuxiliares.add(dataAux);
		});

		List<Thread> threads = new ArrayList<>();
		datasAuxiliares.forEach(data -> {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					LocalDate dataAux = data;
					while (dataAux.isBefore(dataDeFim)) {
						Aula aula = new Aula(dataAux, hrInicioDaAula, hrFimDaAula);
						turma.adicionarAula(aula);
						dataAux = dataAux.plusWeeks(1);
					}
				}
			});
			threads.add(t);
		});
		threads.forEach(t -> t.start());

		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

	}

	private DayOfWeek getDayOfWeek(String nomeDoMetodo) {
		DayOfWeek dayOfWeek = null;
		switch (nomeDoMetodo) {
		case "DOMINGO":
			dayOfWeek = DayOfWeek.SUNDAY;
			break;
		case "SEGUNDA":
			dayOfWeek = DayOfWeek.MONDAY;
			break;
		case "TERCA":
			dayOfWeek = DayOfWeek.TUESDAY;
			break;
		case "QUARTA":
			dayOfWeek = DayOfWeek.WEDNESDAY;
			break;
		case "QUINTA":
			dayOfWeek = DayOfWeek.THURSDAY;
			break;
		case "SEXTA":
			dayOfWeek = DayOfWeek.FRIDAY;
			break;
		case "SABADO":
			dayOfWeek = DayOfWeek.SATURDAY;
			break;
		}

		return dayOfWeek;
	}

}
