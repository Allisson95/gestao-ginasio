package br.com.gestaoginasio.controller.novidade;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestaoginasio.model.Novidade;
import br.com.gestaoginasio.service.NovidadeService;

@Named
@ViewScoped
public class NovidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NovidadeService novidadeService;

	private Novidade novidadeSelecionada;
	private List<Novidade> novidades;
	private List<Novidade> novidadesFiltradas;

	@PostConstruct
	public void abrirPagina() {
		this.buscarTodas();
	}

	private void buscarTodas() {
		this.novidades = this.novidadeService.buscarTodas();
	}

	public String prepararNova() {
		return "CadastrarNovidade.jsf?faces-redirect=true";
	}

	public String editarNovidade() {
		return "CadastrarNovidade.jsf?faces-redirect=true&codigo=" + this.novidadeSelecionada.getCodigo();
	}

	public List<Novidade> getNovidades() {
		return novidades;
	}

	public void setNovidades(List<Novidade> novidades) {
		this.novidades = novidades;
	}

	public List<Novidade> getNovidadesFiltradas() {
		return novidadesFiltradas;
	}

	public void setNovidadesFiltradas(List<Novidade> novidadesFiltradas) {
		this.novidadesFiltradas = novidadesFiltradas;
	}

	public Novidade getNovidadeSelecionada() {
		return novidadeSelecionada;
	}

	public void setNovidadeSelecionada(Novidade novidadeSelecionada) {
		this.novidadeSelecionada = novidadeSelecionada;
	}

}
