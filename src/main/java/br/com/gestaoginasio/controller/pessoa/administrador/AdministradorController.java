package br.com.gestaoginasio.controller.pessoa.administrador;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.service.PessoaService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class AdministradorController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private PessoaService pessoaService;

	private List<Pessoa> administradores;
	private List<Pessoa> administradoresFiltrados;
	private Pessoa administradorSelecionado;

	@PostConstruct
	public void abrirPagina() {
		buscarTodosAdministradores();
	}

	private void buscarTodosAdministradores() {
		this.administradores = this.pessoaService.buscarTodosAdministradores();
	}

	public String prepararNovoAdministrador() {
		return "CadastrarAdministrador.jsf?faces-redirect=true";
	}

	public String editarAdministrador() {
		return "CadastrarAdministrador.jsf?faces-redirect=true&codigo=" + this.administradorSelecionado.getCodigo();
	}

	public void alterarPropriedadeAtivo(Pessoa pessoa) {
		Pessoa pessoaSalvo = this.pessoaService.salvar(pessoa);

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.ativar-desativar", pessoaSalvo.getNome(),
				(pessoaSalvo.getAtivo() ? "ativado" : "desativado")));

		this.buscarTodosAdministradores();
		PrimeFaces.current().ajax().update(Arrays.asList("form:messages-dialog", "form:admins-table"));
	}

	public List<Pessoa> getAdministradores() {
		return administradores;
	}

	public List<Pessoa> getAdministradoresFiltrados() {
		return administradoresFiltrados;
	}

	public void setAdministradoresFiltrados(List<Pessoa> administradoresFiltrados) {
		this.administradoresFiltrados = administradoresFiltrados;
	}

	public Pessoa getAdministradorSelecionado() {
		return administradorSelecionado;
	}

	public void setAdministradorSelecionado(Pessoa administradorSelecionado) {
		this.administradorSelecionado = administradorSelecionado;
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
