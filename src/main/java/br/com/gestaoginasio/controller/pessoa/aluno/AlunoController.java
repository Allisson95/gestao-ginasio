package br.com.gestaoginasio.controller.pessoa.aluno;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.service.AlunoService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class AlunoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private AlunoService alunoService;

	private List<Aluno> alunos;
	private List<Aluno> alunosFiltrados;
	private Aluno alunoSelecionado;

	@PostConstruct
	public void abrirPagina() {
		buscarTodosAlunos();
	}

	private void buscarTodosAlunos() {
		this.alunos = this.alunoService.buscarTodos();
	}

	public String prepararNovoAluno() {
		return "CadastrarAluno.jsf?faces-redirect=true";
	}

	public String editarAluno() {
		return "CadastrarAluno.jsf?faces-redirect=true&codigo=" + this.alunoSelecionado.getCodigo();
	}

	public void alterarPropriedadeAtivo(Aluno aluno) {
		Aluno alunoSalvo = this.alunoService.salvar(aluno);

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.ativar-desativar", alunoSalvo.getNome(),
				(alunoSalvo.getAtivo() ? "ativado" : "desativado")));

		this.buscarTodosAlunos();
		PrimeFaces.current().ajax().update(Arrays.asList("form:messages-dialog", "form:alunos-table"));
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

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
