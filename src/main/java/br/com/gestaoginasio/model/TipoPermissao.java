package br.com.gestaoginasio.model;

public enum TipoPermissao {

	ADMIN("ADMIN"), PROFESSOR("PROFESSOR"), ALUNO("ALUNO");

	private String descricao;

	private TipoPermissao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
