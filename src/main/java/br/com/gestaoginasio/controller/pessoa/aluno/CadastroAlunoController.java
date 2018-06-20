package br.com.gestaoginasio.controller.pessoa.aluno;

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

import br.com.gestaoginasio.model.Aluno;
import br.com.gestaoginasio.model.Cidade;
import br.com.gestaoginasio.model.Estado;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.model.Telefone;
import br.com.gestaoginasio.model.TipoPermissao;
import br.com.gestaoginasio.service.AlunoService;
import br.com.gestaoginasio.service.CidadeService;
import br.com.gestaoginasio.service.EstadoService;
import br.com.gestaoginasio.service.TelefoneService;
import br.com.gestaoginasio.service.UsuarioService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroAlunoController implements Serializable {

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
	private AlunoService alunoService;
	@Inject
	private TelefoneService telefoneService;

	private Aluno edicaoAluno;
	private List<Telefone> telefones = new ArrayList<>(0);
	private List<Telefone> telefonesRemovidos = new ArrayList<>(0);

	private List<Estado> estados;
	private Estado estadoSelecionado;
	private List<Cidade> cidades;

	private Boolean cadastrando = Boolean.TRUE;

	@PostConstruct
	public void abrirPagina() {
		this.estados = this.estadoService.buscarTodos();
		Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		if (parameterMap.containsKey("codigo")) {
			Long codigo = Long.parseLong(parameterMap.get("codigo"));
			this.edicaoAluno = this.alunoService.buscarPeloCodigo(codigo);
			this.telefones = this.telefoneService.buscarContatoDaPessoa(this.edicaoAluno.getCodigo());
			this.estadoSelecionado = this.edicaoAluno.getEndereco().getCidade().getEstado();
			buscarCidadesPeloEstado();
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoAluno = new Aluno();
		}
	}

	public String salvar() {
		this.edicaoAluno = alunoService.salvar(this.edicaoAluno);

		if (!this.telefones.isEmpty()) {
			this.telefones = this.telefoneService.salvarTodos(this.telefones, this.edicaoAluno);
		}
		if (!this.telefonesRemovidos.isEmpty()) {
			this.telefoneService.removerTodos(this.telefonesRemovidos);
		}

		if (this.cadastrando) {
			this.usuarioService.criarESalvarUsuario(this.edicaoAluno, TipoPermissao.ALUNO);
		}

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.salvar", this.edicaoAluno.getNome()));
		this.facesMessages.keepMessagesOnRedirect();

		return "Alunos.jsf?faces-redirect=true";
	}

	public void adicionarTelefone() {
		this.telefones.add(new Telefone());
	}

	public void removerTelefone(Telefone telefone) {
		if (this.telefones.remove(telefone)) {
			telefonesRemovidos.add(telefone);
		}
	}

	public Aluno getEdicaoAluno() {
		return edicaoAluno;
	}

	public void setEdicaoAluno(Aluno edicaoAluno) {
		this.edicaoAluno = edicaoAluno;
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
		if (StringUtils.isNotBlank(this.edicaoAluno.getEndereco().getCep())
				&& this.edicaoAluno.getEndereco().getCep().length() == 8) {
			try {
				ViaCepWebService cepWebService = new ViaCepWebService(this.edicaoAluno.getEndereco().getCep());
				EnderecoViaCep enderecoViaCep = cepWebService.getEnderecoViaCep();
				this.edicaoAluno.getEndereco().setLogradouro(enderecoViaCep.getLogradouro());
				this.edicaoAluno.getEndereco().setComplemento(enderecoViaCep.getComplemento());
				this.edicaoAluno.getEndereco().setBairro(enderecoViaCep.getBairro());
				this.estadoSelecionado = this.estadoService.buscarPelaSigla(enderecoViaCep.getUf().toUpperCase());
				buscarCidadesPeloEstado();
				this.edicaoAluno.getEndereco().setCidade(
						this.cidadeService.buscarPorNomeEUf(enderecoViaCep.getLocalidade(), this.estadoSelecionado));
			} catch (Exception e) {
			}
		}
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
