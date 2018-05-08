package br.com.gestaoginasio.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "turma")
public class Turma implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short codigo;
	private String identificacao;
	private LocalDate dtInicio;
	private LocalDate dtFim;
	private Year ano;
	private Boolean ativo = Boolean.TRUE;
	private Modalidade modalidade;
	private Periodicidade periodicidade;
	private Set<Professor> professores = new HashSet<>(0);
	private Set<Aluno> alunos = new HashSet<>(0);
	private Set<Aula> aulas = new HashSet<>(0);
	private List<Mensagem> mensagens = new ArrayList<>(0);

	public Turma() {
		this.periodicidade = new Periodicidade();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Short getCodigo() {
		return codigo;
	}

	public void setCodigo(Short codigo) {
		this.codigo = codigo;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	@NotNull(message = "Data de inicio é obrigatória.")
	@Column(name = "dt_inicio")
	public LocalDate getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}

	@NotNull(message = "Data de fim é obrigatória.")
	@Column(name = "dt_fim")
	public LocalDate getDtFim() {
		return dtFim;
	}

	public void setDtFim(LocalDate dtFim) {
		this.dtFim = dtFim;
	}

	public Year getAno() {
		return ano;
	}

	public void setAno(Year ano) {
		this.ano = ano;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@NotNull(message = "Modalidade é obrigatório.")
	@ManyToOne
	@JoinColumn(name = "codigo_modalidade")
	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "codigo_periodicidade")
	public Periodicidade getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(Periodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}

	@NotEmpty(message = "Selecione pelo menos um professor.")
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "professor_ministra_turma", joinColumns = {
			@JoinColumn(name = "codigo_turma") }, inverseJoinColumns = {
					@JoinColumn(name = "codigo_pessoa_professor") })
	public Set<Professor> getProfessores() {
		return professores;
	}

	public void setProfessores(Set<Professor> professores) {
		this.professores = professores;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "aluno_matricula_turma", joinColumns = {
			@JoinColumn(name = "codigo_turma") }, inverseJoinColumns = { @JoinColumn(name = "codigo_pessoa_aluno") })
	public Set<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(Set<Aluno> alunos) {
		this.alunos = alunos;
	}

	public Aluno adicionarAluno(Aluno aluno) {
		getAlunos().add(aluno);
		return aluno;
	}

	@Valid
	@OneToMany(mappedBy = "turma", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	public Set<Aula> getAulas() {
		return Collections.synchronizedSet(aulas);
	}

	public void setAulas(Set<Aula> aulas) {
		this.aulas = aulas;
	}

	public Aula adicionarAula(Aula aula) {
		aula.setTurma(this);
		getAulas().add(aula);

		return aula;
	}

	public Aula removerAula(Aula aula) {
		aula.setTurma(null);
		getAulas().remove(aula);

		return aula;
	}

	@ManyToMany(mappedBy = "turmas")
	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
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
		Turma other = (Turma) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
