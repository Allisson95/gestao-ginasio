package br.com.gestaoginasio.repository;

import br.com.gestaoginasio.model.Usuario;
import br.com.gestaoginasio.repository.contrato.RepositoryImpl;

public class UsuarioRepository extends RepositoryImpl<Usuario, Long> {

	public String ultimaMatricula() {
		return super.entityManager
				.createQuery("SELECT u.matricula FROM Usuario AS u ORDER BY u.codigo DESC", String.class)
				.setMaxResults(1).getSingleResult();
	}
	
	public Usuario buscarUsuarioPelaMatricula(String matricula) {
		return super.buscar("FROM Usuario WHERE matricula = ?0", matricula);
	}

}
