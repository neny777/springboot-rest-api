package br.com.supermidia.material;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, UUID> {
	
	boolean existsById(UUID id);

	boolean existsByNomeAndMarca(String nome, String marca);
	
	boolean existsByNomeAndMarcaAndIdNot(String nome, String marca, UUID id);
}
