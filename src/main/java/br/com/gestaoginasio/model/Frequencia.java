package br.com.gestaoginasio.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "frequencia")
public class Frequencia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private Long codigoAlunoMatriculaTurma;
	private Short codigoTurmaMatriculaTurma;
	private Boolean presente = Boolean.FALSE;
	private Aula aula;
	private Desempenho desempenho;

	public Frequencia() {
		this.desempenho = new Desempenho();
	}

	public Frequencia(Aluno aluno, Turma turma, Aula aula) {
		this.codigoAlunoMatriculaTurma = aluno.getCodigo();
		this.codigoTurmaMatriculaTurma = turma.getCodigo();
		this.aula = aula;
		this.desempenho = new Desempenho();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Column(name = "codigo_aluno_matricula_turma")
	public Long getCodigoAlunoMatriculaTurma() {
		return this.codigoAlunoMatriculaTurma;
	}

	public void setCodigoAlunoMatriculaTurma(Long codigoAlunoMatriculaTurma) {
		this.codigoAlunoMatriculaTurma = codigoAlunoMatriculaTurma;
	}

	@Column(name = "codigo_turma_matricula_turma")
	public Short getCodigoTurmaMatriculaTurma() {
		return this.codigoTurmaMatriculaTurma;
	}

	public void setCodigoTurmaMatriculaTurma(Short codigoTurmaMatriculaTurma) {
		this.codigoTurmaMatriculaTurma = codigoTurmaMatriculaTurma;
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aula")
	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "codigo_desempenho")
	public Desempenho getDesempenho() {
		return this.desempenho;
	}

	public void setDesempenho(Desempenho desempenho) {
		this.desempenho = desempenho;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Frequencia)) {
			return false;
		}
		Frequencia other = (Frequencia) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}
