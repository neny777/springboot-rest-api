package br.com.supermidia.pessoa.colaborador;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID> {
	
	boolean existsByFisicaId(UUID id);

	boolean existsByFisicaNome(String nome);

	boolean existsByFisicaEmail(String email);

	boolean existsByFisicaTelefone(String telefone);

	boolean existsByFisicaNomeAndIdNot(String nome, UUID id);

	boolean existsByFisicaEmailAndIdNot(String email, UUID id);

	boolean existsByFisicaTelefoneAndIdNot(String telefone, UUID id);

	Colaborador getByFisicaNome(String nome);

	Colaborador getByFisicaEmail(String email);

	Colaborador getByFisicaTelefone(String telefone);
}
