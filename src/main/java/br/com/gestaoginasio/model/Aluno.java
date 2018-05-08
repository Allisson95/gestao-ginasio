package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "aluno")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "codigo_pessoa")
public class Aluno extends Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<Turma> turmas = new HashSet<>(0);

	public Aluno() {
		super();
	}

	@ManyToMany(mappedBy = "alunos", cascade = CascadeType.ALL)
	public Set<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

}
