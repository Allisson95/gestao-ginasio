package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "aula")
public class Aula implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private LocalDate dtAula;
	private LocalTime hrInicio;
	private LocalTime hrFim;
	private Turma turma;
	private Set<Frequencia> frequencias = new HashSet<>(0);

	public Aula() {
	}

	public Aula(LocalDate dtAula, LocalTime hrInicio, LocalTime hrFim) {
		this.dtAula = dtAula;
		this.hrInicio = hrInicio;
		this.hrFim = hrFim;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Column(name = "dt_aula")
	public LocalDate getDtAula() {
		return dtAula;
	}

	public void setDtAula(LocalDate dtAula) {
		this.dtAula = dtAula;
	}

	@NotNull(message = "Hora de Inicio é obrigatório.")
	@Column(name = "hr_inicio")
	public LocalTime getHrInicio() {
		return hrInicio;
	}

	public void setHrInicio(LocalTime hrInicio) {
		this.hrInicio = hrInicio;
	}

	@NotNull(message = "Hora de Fim é obrigatório.")
	@Column(name = "hr_fim")
	public LocalTime getHrFim() {
		return hrFim;
	}

	public void setHrFim(LocalTime hrFim) {
		this.hrFim = hrFim;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "codigo_turma")
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Turma adicionarTurma(Turma turma) {
		turma.getAulas().add(this);
		setTurma(turma);

		return turma;
	}

	public Turma removerTurma(Turma turma) {
		turma.getAulas().remove(this);
		setTurma(null);

		return turma;
	}

	@OneToMany(mappedBy = "aula")
	public Set<Frequencia> getFrequencias() {
		return frequencias;
	}

	public void setFrequencias(Set<Frequencia> frequencias) {
		this.frequencias = frequencias;
	}

	public Frequencia adicionarFrequencia(Frequencia frequencia) {
		getFrequencias().add(frequencia);
		frequencia.setAula(this);

		return frequencia;
	}

	public Frequencia removerFrequencia(Frequencia frequencia) {
		getFrequencias().remove(frequencia);
		frequencia.setAula(null);

		return frequencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((dtAula == null) ? 0 : dtAula.hashCode());
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
		if (!(obj instanceof Aula)) {
			return false;
		}
		Aula other = (Aula) obj;
		if (codigo == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}
		if (dtAula == null) {
			if (other.getDtAula() != null) {
				return false;
			}
		} else if (!dtAula.equals(other.getDtAula())) {
			return false;
		}
		return true;
	}

}
