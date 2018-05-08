package br.com.gestaoginasio.controller.pessoa.professor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.github.allisson95.viacepclient.EnderecoViaCep;
import com.github.allisson95.viacepclient.ViaCepWebService;

import br.com.gestaoginasio.model.Cidade;
import br.com.gestaoginasio.model.Estado;
import br.com.gestaoginasio.model.Modalidade;
import br.com.gestaoginasio.model.Professor;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.model.Telefone;
import br.com.gestaoginasio.model.TipoPermissao;
import br.com.gestaoginasio.model.Usuario;
import br.com.gestaoginasio.service.CidadeService;
import br.com.gestaoginasio.service.EstadoService;
import br.com.gestaoginasio.service.ModalidadeService;
import br.com.gestaoginasio.service.ProfessorService;
import br.com.gestaoginasio.service.TelefoneService;
import br.com.gestaoginasio.service.UsuarioService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroProfessorController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private EstadoService estadoService;
	@Inject
	private CidadeService cidadeService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private ProfessorService professorService;
	@Inject
	private ModalidadeService modalidadeService;
	@Inject
	private TelefoneService telefoneService;

	private Professor edicaoProfessor;
	private List<Telefone> telefones = new ArrayList<>(0);
	private List<Telefone> telefonesRemovidos = new ArrayList<>(0);

	private List<Estado> estados;
	private Estado estadoSelecionado;
	private List<Cidade> cidades;

	private List<Modalidade> modalidades;
	private List<Modalidade> modalidadesSelecionadas;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		this.estados = this.estadoService.buscarTodos();
		this.modalidades = this.modalidadeService.buscarTodas();
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Long codigo = Long.parseLong(parameterMap.get("codigo"));
			this.edicaoProfessor = this.professorService.buscarPeloCodigo(codigo);
			this.telefones = this.telefoneService.buscarContatoDaPessoa(this.edicaoProfessor.getCodigo());
			this.estadoSelecionado = this.edicaoProfessor.getEndereco().getCidade().getEstado();
			buscarCidadesPeloEstado();
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoProfessor = new Professor();
		}
	}

	public String salvar() {
		this.edicaoProfessor = professorService.salvar(this.edicaoProfessor);

		if (!this.telefones.isEmpty()) {
			this.telefones = this.telefoneService.salvarTodos(this.telefones, this.edicaoProfessor);
		}
		if (!this.telefonesRemovidos.isEmpty()) {
			this.telefoneService.removerTodos(this.telefonesRemovidos);
		}

		if (this.cadastrando) {
			Usuario novoUsuario = this.usuarioService.criarNovoUsuario(this.edicaoProfessor, TipoPermissao.PROFESSOR);
			novoUsuario = this.usuarioService.salvar(novoUsuario);
		}

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.salvar", this.edicaoProfessor.getNome()));
		this.facesMessages.keepMessagesOnRedirect();

		return "Professores.jsf?faces-redirect=true";
	}

	public void adicionarTelefone() {
		this.telefones.add(new Telefone());
	}

	public void removerTelefone(Telefone telefone) {
		if (this.telefones.remove(telefone)) {
			telefonesRemovidos.add(telefone);
		}
	}

	public Professor getEdicaoProfessor() {
		return edicaoProfessor;
	}

	public void setEdicaoProfessor(Professor edicaoProfessor) {
		this.edicaoProfessor = edicaoProfessor;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public List<Modalidade> getModalidades() {
		return modalidades;
	}

	public List<Modalidade> getModalidadesSelecionadas() {
		return modalidadesSelecionadas;
	}

	public void setModalidadesSelecionadas(List<Modalidade> modalidadesSelecionadas) {
		this.modalidadesSelecionadas = modalidadesSelecionadas;
	}

	public Boolean getCadastrando() {
		return cadastrando;
	}

	public void buscarCidadesPeloEstado() {
		if (this.estadoSelecionado != null) {
			this.cidades = this.cidadeService.buscarCidadesPeloEstado(this.estadoSelecionado);
		} else {
			this.cidades = new ArrayList<>();
		}
	}

	public void buscarCep() {
		if (StringUtils.isNotBlank(this.edicaoProfessor.getEndereco().getCep())
				&& this.edicaoProfessor.getEndereco().getCep().length() == 8) {
			try {
				ViaCepWebService cepWebService = new ViaCepWebService(this.edicaoProfessor.getEndereco().getCep());
				EnderecoViaCep enderecoViaCep = cepWebService.getEnderecoViaCep();
				this.edicaoProfessor.getEndereco().setLogradouro(enderecoViaCep.getLogradouro());
				this.edicaoProfessor.getEndereco().setComplemento(enderecoViaCep.getComplemento());
				this.edicaoProfessor.getEndereco().setBairro(enderecoViaCep.getBairro());
				this.estadoSelecionado = this.estadoService.buscarPelaSigla(enderecoViaCep.getUf().toUpperCase());
				buscarCidadesPeloEstado();
				this.edicaoProfessor.getEndereco().setCidade(
						this.cidadeService.buscarPorNomeEUf(enderecoViaCep.getLocalidade(), this.estadoSelecionado));
			} catch (Exception e) {
			}
		}
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
