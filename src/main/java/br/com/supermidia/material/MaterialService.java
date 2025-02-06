package br.com.supermidia.material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {
	@Autowired
	private MaterialRepository materialRepository;

	public List<Material> findAll() {
		return materialRepository.findAll();
	}

	public Material findById(UUID id) {
		return materialRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Material não encontrado."));
	}

	public boolean existsById(UUID id) {
		return materialRepository.existsById(id);
	}

	public Material create(Material material) {
		return materialRepository.save(material);
	}

	public Material update(UUID id, Material material) {
		Material existingMaterial = findById(id);
		existingMaterial.setNome(material.getNome());
		existingMaterial.setMarca(material.getMarca());		
		existingMaterial.setUnidade(material.getUnidade());
		existingMaterial.setPreco(material.getPreco());
		return materialRepository.save(existingMaterial);
	}

	public void delete(UUID id) {
		Material material = findById(id);
		materialRepository.delete(material);
	}

	public List<String> materialUniqueAttributeValidation(Material material) {

		List<String> erros = new ArrayList<>();

		if (material.getId() == null) {
			// Verifica duplicidade ao criar novo Material
			uniquenessValidation(material.getNome(), material.getMarca(), null, "Nome e Marca",
					(nome, marca) -> materialRepository.existsByNomeAndMarca(nome, marca), erros);
		} else {
			UUID id = material.getId();
			// Verifica duplicidade ao editar um Material existente
			uniquenessValidation(material.getNome(), material.getMarca(), id, "Nome e Marca",
					(nome, marca) -> materialRepository.existsByNomeAndMarcaAndIdNot(nome, marca, id), erros);
		}

		return erros;
	}

	private void uniquenessValidation(String nome, String marca, UUID id, String campo,
			BiFunction<String, String, Boolean> checkUniqueness, List<String> erros) {
		if ((nome == null || nome.isBlank()) || (marca == null || marca.isBlank())) {
			return; // Ignora valores nulos ou vazios
		}
		// Verifica se a combinação nome + marca já existe no banco
		boolean duplicado = checkUniqueness.apply(nome, marca);
		if (duplicado) {
			erros.add(campo.toUpperCase() + " " + nome + " " + marca + " já está cadastrado");
		}
	}
}
