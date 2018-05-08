package br.com.gestaoginasio.controller.turma;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.exception.GestaoGinasioException;
import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.model.Turma;
import br.com.gestaoginasio.service.AlunoService;
import br.com.gestaoginasio.service.TurmaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class MatriculaAlunoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private TurmaService turmaService;
	@Inject
	private AlunoService alunoService;

	private List<Aluno> alunos;
	private List<Aluno> alunosFiltrados;
	private Aluno alunoSelecionado;

	private Turma edicaoTurma;

	@PostConstruct
	public void abrirPagina() {
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Short codigo = Short.parseShort(parameterMap.get("codigo"));
			this.edicaoTurma = this.turmaService.buscarPeloCodigo(codigo);
		}
		this.alunos = this.alunoService.buscarTodos();
	}

	public String matricular() {
		if (this.alunoSelecionado != null) {
			try {
				Aluno alunoMatriculado = this.turmaService.matricularAlunoNaTurma(this.alunoSelecionado,
						this.edicaoTurma);
				this.facesMessages
						.info(this.messagesBundle.getMessage("turma.matricular-aluno", alunoMatriculado.getNome()));
				this.facesMessages.keepMessagesOnRedirect();
				return "Turmas.jsf?faces-redirect=true";
			} catch (GestaoGinasioException e) {
				this.facesMessages.warn(e.getMessage());
				return "";
			}
		} else {
			this.facesMessages.warn("Selecione um aluno.");
			return "";
		}
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public List<Aluno> getAlunosFiltrados() {
		return alunosFiltrados;
	}

	public void setAlunosFiltrados(List<Aluno> alunosFiltrados) {
		this.alunosFiltrados = alunosFiltrados;
	}

	public Aluno getAlunoSelecionado() {
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Aluno alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

	public Turma getEdicaoTurma() {
		return edicaoTurma;
	}

	public void setEdicaoTurma(Turma edicaoTurma) {
		this.edicaoTurma = edicaoTurma;
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
