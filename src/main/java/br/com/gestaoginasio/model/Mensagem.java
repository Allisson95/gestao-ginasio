package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mensagem")
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private String titulo;
	private String mensagem;
	private LocalDateTime dtPostagem;
	private List<Turma> turmas = new ArrayList<>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@NotBlank(message = "Título é obrigatório.")
	@Size(max = 200, message = "Tamanho máximo do título é de {max} caracteres.")
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@NotBlank(message = "Mensagem é obrigatório.")
	@Size(max = 10000, message = "Tamanho máximo da mensagem é de {max} caracteres.")
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Column(name = "dt_postagem")
	public LocalDateTime getDtPostagem() {
		return dtPostagem;
	}

	public void setDtPostagem(LocalDateTime dtPostagem) {
		this.dtPostagem = dtPostagem;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "mensagem_da_turma", joinColumns = {
			@JoinColumn(name = "codigo_mensagem") }, inverseJoinColumns = { @JoinColumn(name = "codigo_turma") })
	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mensagem other = (Mensagem) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
