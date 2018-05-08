package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "professor")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "codigo_pessoa")
public class Professor extends Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<Modalidade> modalidades = new HashSet<>(0);
	private Set<Turma> turmas = new HashSet<>(0);

	public Professor() {
		super();
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "professor_leciona_modalidade", joinColumns = {
			@JoinColumn(name = "codigo_pessoa_professor") }, inverseJoinColumns = {
					@JoinColumn(name = "codigo_modalidade") })
	public Set<Modalidade> getModalidades() {
		return modalidades;
	}

	public void setModalidades(Set<Modalidade> modalidades) {
		this.modalidades = modalidades;
	}

	@ManyToMany(mappedBy = "professores", cascade = CascadeType.ALL)
	public Set<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

}
