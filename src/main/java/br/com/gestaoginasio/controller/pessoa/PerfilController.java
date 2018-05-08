package br.com.gestaoginasio.controller.pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.github.allisson95.viacepclient.EnderecoViaCep;
import com.github.allisson95.viacepclient.ViaCepWebService;

import br.com.gestaoginasio.auth.principal.Principal;
import br.com.gestaoginasio.model.Cidade;
import br.com.gestaoginasio.model.Estado;
import br.com.gestaoginasio.model.Pessoa;
import br.com.gestaoginasio.model.Telefone;
import br.com.gestaoginasio.service.CidadeService;
import br.com.gestaoginasio.service.EstadoService;
import br.com.gestaoginasio.service.PessoaService;
import br.com.gestaoginasio.service.TelefoneService;
import br.com.gestaoginasio.service.UsuarioService;
import br.com.gestaoginasio.util.FacesMessages;
import br.com.gestaoginasio.util.MessagesBundle;

@Named
@ViewScoped
public class PerfilController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MessagesBundle messagesBundle;
	@Inject
	private FacesMessages facesMessages;
	@Inject
	private Principal principal;
	@Inject
	private EstadoService estadoService;
	@Inject
	private CidadeService cidadeService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private TelefoneService telefoneService;

	private Pessoa edicaoPessoa;
	private List<Telefone> telefones = new ArrayList<>(0);
	private List<Telefone> telefonesRemovidos = new ArrayList<>(0);
	private String novaSenha;
	private Boolean modificarSenha = Boolean.FALSE;

	private List<Estado> estados;
	private Estado estadoSelecionado;
	private List<Cidade> cidades;

	@PostConstruct
	public void abrirPagina() {
		this.estados = this.estadoService.buscarTodos();
		this.edicaoPessoa = pessoaService.buscarPeloCodigo(principal.getCodigo());
		this.telefones = this.telefoneService.buscarContatoDaPessoa(this.edicaoPessoa.getCodigo());
		this.estadoSelecionado = this.edicaoPessoa.getEndereco().getCidade().getEstado();
		buscarCidadesPeloEstado();
	}

	public void buscarCidadesPeloEstado() {
		if (this.estadoSelecionado != null) {
			this.cidades = this.cidadeService.buscarCidadesPeloEstado(this.estadoSelecionado);
		} else {
			this.cidades = new ArrayList<>();
		}
	}

	public void modificarSenha() {
		this.modificarSenha = !this.modificarSenha;
	}

	public void salvar() {
		if (this.modificarSenha && StringUtils.isNotBlank(this.novaSenha)
				&& !this.usuarioService.senhasIguais(this.novaSenha, this.edicaoPessoa.getUsuario().getSenha())) {
			this.edicaoPessoa.getUsuario().setSenha(this.usuarioService.criptografarSenha(this.novaSenha));
			this.edicaoPessoa.setUsuario(this.usuarioService.salvar(this.edicaoPessoa.getUsuario()));
		}
		this.pessoaService.salvar(this.edicaoPessoa);

		if (!this.telefones.isEmpty()) {
			this.telefones = this.telefoneService.salvarTodos(this.telefones, this.edicaoPessoa);
		}
		if (!this.telefonesRemovidos.isEmpty()) {
			this.telefoneService.removerTodos(this.telefonesRemovidos);
		}

		if (this.modificarSenha) {
			this.modificarSenha = !this.modificarSenha;
		}

		this.facesMessages.info(this.messagesBundle.getMessage("perfil.atualizado"));
	}

	public void adicionarTelefone() {
		this.telefones.add(new Telefone());
	}

	public void removerTelefone(Telefone telefone) {
		if (this.telefones.remove(telefone)) {
			telefonesRemovidos.add(telefone);
		}
	}

	public Pessoa getEdicaoPessoa() {
		return edicaoPessoa;
	}

	public void setEdicaoPessoa(Pessoa edicaoPessoa) {
		this.edicaoPessoa = edicaoPessoa;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public Boolean getModificarSenha() {
		return modificarSenha;
	}

	public void setModificarSenha(Boolean modificarSenha) {
		this.modificarSenha = modificarSenha;
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

	public void buscarCep() {
		if (StringUtils.isNotBlank(this.edicaoPessoa.getEndereco().getCep())
				&& this.edicaoPessoa.getEndereco().getCep().length() == 8) {
			try {
				ViaCepWebService cepWebService = new ViaCepWebService(this.edicaoPessoa.getEndereco().getCep());
				EnderecoViaCep enderecoViaCep = cepWebService.getEnderecoViaCep();
				this.edicaoPessoa.getEndereco().setLogradouro(enderecoViaCep.getLogradouro());
				this.edicaoPessoa.getEndereco().setComplemento(enderecoViaCep.getComplemento());
				this.edicaoPessoa.getEndereco().setBairro(enderecoViaCep.getBairro());
				this.estadoSelecionado = this.estadoService.buscarPelaSigla(enderecoViaCep.getUf().toUpperCase());
				buscarCidadesPeloEstado();
				this.edicaoPessoa.getEndereco().setCidade(
						this.cidadeService.buscarPorNomeEUf(enderecoViaCep.getLocalidade(), this.estadoSelecionado));
			} catch (Exception e) {
			}
		}
	}

}
