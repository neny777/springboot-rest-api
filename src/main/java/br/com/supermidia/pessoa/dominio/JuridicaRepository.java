package br.com.supermidia.pessoa.dominio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JuridicaRepository extends JpaRepository<Juridica, UUID> {
	Optional<Juridica> findByCnpj(String cnpj);

	Optional<Juridica> findByEmail(String email);

	Optional<Juridica> findByTelefone(String telefone);

	Optional<Juridica> findByNome(String nome);

	@Query("SELECT j FROM Juridica j WHERE " + "(:nome IS NULL OR j.nome = :nome) AND "
			+ "(:email IS NULL OR j.email = :email) AND " + "(:telefone IS NULL OR j.telefone = :telefone) AND "
			+ "(:cnpj IS NULL OR j.cnpj = :cnpj)")
	Optional<Juridica> findByAttributes(@Param("nome") String nome, @Param("email") String email,
			@Param("telefone") String telefone, @Param("cnpj") String cnpj);

	@Query("SELECT f FROM Fisica f WHERE f.id = :id")
	Optional<Fisica> findFisicaById(@Param("id") UUID id);

	@Query("SELECT j FROM Juridica j WHERE j.id = :id")
	Optional<Juridica> findJuridicaById(@Param("id") UUID id);
}
