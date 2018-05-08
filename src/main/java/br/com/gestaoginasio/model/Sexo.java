package br.com.gestaoginasio.model;

public enum Sexo {

	FEMININO("FEMININO"), MASCULINO("MASCULINO");

	private String descricao;

	Sexo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
