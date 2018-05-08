package br.com.gestaoginasio.repository.contrato;

public class Paginavel {

	private Short pagina;
	private Integer primeiroItemDaPagina;
	private Integer resultadoMaximo;

	public Paginavel(Short pagina, Integer primeiroItemDaPagina, Integer resultadoMaximo) {
		this.pagina = pagina;
		this.primeiroItemDaPagina = primeiroItemDaPagina;
		this.resultadoMaximo = resultadoMaximo;
	}

	public Short getPagina() {
		return pagina;
	}

	public void setPagina(Short pagina) {
		this.pagina = pagina;
	}

	public Integer getPrimeiroItemDaPagina() {
		return primeiroItemDaPagina;
	}

	public void setPrimeiroItemDaPagina(Integer primeiroItemDaPagina) {
		this.primeiroItemDaPagina = primeiroItemDaPagina;
	}

	public Integer getResultadoMaximo() {
		return resultadoMaximo;
	}

	public void setResultadoMaximo(Integer resultadoMaximo) {
		this.resultadoMaximo = resultadoMaximo;
	}

}
