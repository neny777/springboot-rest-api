package br.com.supermidia.pessoa.colaborador;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID> {
	boolean existsByFisicaNome(String nome);
	boolean existsByFisicaEmail(String email);
	boolean existsByFisicaTelefone(String telefone);
	boolean existsByFisicaCpf(String cpf);
	boolean existsByFisicaRg(String rg);
	boolean existsByCtps(String ctps);
}
