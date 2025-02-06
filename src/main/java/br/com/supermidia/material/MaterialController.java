package br.com.supermidia.material;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

	private final MaterialService materialService;

	public MaterialController(MaterialService materialService) {
		this.materialService = materialService;
	}

	// Create a new material
	@PostMapping
	public ResponseEntity<Material> createMaterial(@Valid @RequestBody Material material) {
		Material created = materialService.create(material);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// Retrieve a material by id
	@GetMapping("/{id}")
	public ResponseEntity<Material> getMaterial(@PathVariable UUID id) {
		Material material = materialService.findById(id);
		return ResponseEntity.ok(material);
	}

	// Retrieve all materials
	@GetMapping
	public ResponseEntity<List<Material>> getAllMaterials() {
		List<Material> materiais = materialService.findAll();
		return ResponseEntity.ok(materiais);
	}

	// Update an existing material
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMaterial(@PathVariable UUID id, @Valid @RequestBody Material material) {
		List<String> erros = materialService.materialUniqueAttributeValidation(material);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		materialService.update(id, material);
		return ResponseEntity.ok("Material atualizado com sucesso!");
	}

	// Delete a material by id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMaterial(@PathVariable UUID id) {
		materialService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/validar")
	public ResponseEntity<?> validarMaterial(@RequestBody @Valid Material material) {
		// Realiza a validação
		List<String> erros = materialService.materialUniqueAttributeValidation(material);

		System.out.println("Erros:");

		for (String string : erros) {
			System.out.println(string);
		}

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (material.getId() != null) {
			boolean cadastrado = materialService.existsById(material.getId());
			if (cadastrado) {
				erros.add("Material já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}
}
