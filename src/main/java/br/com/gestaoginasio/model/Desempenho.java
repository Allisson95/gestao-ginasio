package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "desempenho")
public class Desempenho implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private Integer avaliacao;
	private String observacao;
	private Set<Frequencia> frequencias;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getAvaliacao() {
		return this.avaliacao;
	}

	public void setAvaliacao(Integer avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@OneToMany(mappedBy = "desempenho")
	public Set<Frequencia> getFrequencias() {
		return this.frequencias;
	}

	public void setFrequencias(Set<Frequencia> frequencias) {
		this.frequencias = frequencias;
	}

	public Frequencia addFrequencia(Frequencia frequencia) {
		getFrequencias().add(frequencia);
		frequencia.setDesempenho(this);

		return frequencia;
	}

	public Frequencia removeFrequencia(Frequencia frequencia) {
		getFrequencias().remove(frequencia);
		frequencia.setDesempenho(null);

		return frequencia;
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
		if (!(obj instanceof Desempenho)) {
			return false;
		}
		Desempenho other = (Desempenho) obj;
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