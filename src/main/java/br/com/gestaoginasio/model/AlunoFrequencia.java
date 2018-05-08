package br.com.gestaoginasio.model;

public class AlunoFrequencia {

	private Aluno aluno;
	private Frequencia frequencia;

	public AlunoFrequencia(Aluno aluno, Frequencia frequencia) {
		this.aluno = aluno;
		this.frequencia = frequencia;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Frequencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

}