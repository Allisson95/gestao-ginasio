package br.com.gestaoginasio.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "periodicidade")
public class Periodicidade implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private DiasSemana diasSemana;
	private Turma turma;

	public Periodicidade() {
		this.diasSemana = new DiasSemana();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Embedded
	public DiasSemana getDiasSemana() {
		return diasSemana;
	}

	public void setDiasSemana(DiasSemana diasSemana) {
		this.diasSemana = diasSemana;
	}

	@OneToOne(mappedBy = "periodicidade")
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
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
		if (!(obj instanceof Periodicidade)) {
			return false;
		}
		Periodicidade other = (Periodicidade) obj;
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
