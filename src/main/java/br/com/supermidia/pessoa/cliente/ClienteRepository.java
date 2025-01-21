package br.com.supermidia.pessoa.cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
	boolean existsByPessoaId(UUID pessoaId);

	@Query(value = """
			    SELECT BIN_TO_UUID(c.pessoa_id) AS clienteId,
			           p.nome AS nome,
			           p.email AS email,
			           p.telefone AS telefone,
			           p.municipio AS municipio,
			           p.uf AS uf,
			           c.tipo AS tipo
			    FROM clientes c
			    JOIN pessoas p ON c.pessoa_id = p.id
			    LEFT JOIN pessoas_fisica f ON p.id = f.id
			    LEFT JOIN pessoas_juridica j ON p.id = j.id
			""", nativeQuery = true)
	List<Object[]> findAllClientes();

	@Query("SELECT c FROM Cliente c WHERE c.pessoa.id = :pessoaId")
	Optional<Cliente> findByPessoaId(@Param("pessoaId") UUID pessoaId);
}
