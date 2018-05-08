package br.com.gestaoginasio.controller.pessoa.administrador;

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
import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.Sexo;
import br.com.gestaoginasio.model.Telefone;
import br.com.gestaoginasio.model.TipoPermissao;
import br.com.gestaoginasio.model.Usuario;
import br.com.gestaoginasio.service.CidadeService;
import br.com.gestaoginasio.service.EstadoService;
import br.com.gestaoginasio.service.PessoaService;
import br.com.gestaoginasio.service.TelefoneService;
import br.com.gestaoginasio.service.UsuarioService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class CadastroAdministradorController implements Serializable {

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
	private PessoaService pessoaService;
	@Inject
	private TelefoneService telefoneService;

	private Pessoa edicaoAdministrador;
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
			this.edicaoAdministrador = this.pessoaService.buscarPeloCodigo(codigo);
			this.telefones = this.telefoneService.buscarContatoDaPessoa(this.edicaoAdministrador.getCodigo());
			this.estadoSelecionado = this.edicaoAdministrador.getEndereco().getCidade().getEstado();
			buscarCidadesPeloEstado();
			this.cadastrando = Boolean.FALSE;
		} else {
			this.edicaoAdministrador = new Pessoa();
		}
	}

	public String salvar() {
		this.edicaoAdministrador = pessoaService.salvar(this.edicaoAdministrador);

		if (!this.telefones.isEmpty()) {
			this.telefones = this.telefoneService.salvarTodos(this.telefones, this.edicaoAdministrador);
		}
		if (!this.telefonesRemovidos.isEmpty()) {
			this.telefoneService.removerTodos(this.telefonesRemovidos);
		}

		if (this.cadastrando) {
			Usuario novoUsuario = this.usuarioService.criarNovoUsuario(this.edicaoAdministrador, TipoPermissao.ADMIN);
			novoUsuario = this.usuarioService.salvar(novoUsuario);
		}

		this.facesMessages.info(this.messagesBundle.getMessage("pessoa.salvar", this.edicaoAdministrador.getNome()));
		this.facesMessages.keepMessagesOnRedirect();

		return "Administradores.jsf?faces-redirect=true";
	}

	public void adicionarTelefone() {
		this.telefones.add(new Telefone());
	}

	public void removerTelefone(Telefone telefone) {
		if (this.telefones.remove(telefone)) {
			telefonesRemovidos.add(telefone);
		}
	}

	public Pessoa getEdicaoAdministrador() {
		return edicaoAdministrador;
	}

	public void setEdicaoAdministrador(Pessoa edicaoAdministrador) {
		this.edicaoAdministrador = edicaoAdministrador;
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
		if (StringUtils.isNotBlank(this.edicaoAdministrador.getEndereco().getCep())
				&& this.edicaoAdministrador.getEndereco().getCep().length() == 8) {
			try {
				ViaCepWebService cepWebService = new ViaCepWebService(this.edicaoAdministrador.getEndereco().getCep());
				EnderecoViaCep enderecoViaCep = cepWebService.getEnderecoViaCep();
				this.edicaoAdministrador.getEndereco().setLogradouro(enderecoViaCep.getLogradouro());
				this.edicaoAdministrador.getEndereco().setComplemento(enderecoViaCep.getComplemento());
				this.edicaoAdministrador.getEndereco().setBairro(enderecoViaCep.getBairro());
				this.estadoSelecionado = this.estadoService.buscarPelaSigla(enderecoViaCep.getUf().toUpperCase());
				buscarCidadesPeloEstado();
				this.edicaoAdministrador.getEndereco().setCidade(
						this.cidadeService.buscarPorNomeEUf(enderecoViaCep.getLocalidade(), this.estadoSelecionado));
			} catch (Exception e) {
			}
		}
	}

	public Sexo[] getSexo() {
		return Sexo.values();
	}

}
