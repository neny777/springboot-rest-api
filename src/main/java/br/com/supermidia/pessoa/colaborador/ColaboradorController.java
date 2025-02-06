package br.com.supermidia.pessoa.colaborador;

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
@RequestMapping("/api/colaboradores")
public class ColaboradorController {
	private final ColaboradorService colaboradorService;

	public ColaboradorController(ColaboradorService colaboradorService) {
		this.colaboradorService = colaboradorService;
	}
	// Buscar pessoa física
	@GetMapping("/fisica/{id}")
	public ResponseEntity<ColaboradorDTO> findPessoaFisicaById(@PathVariable UUID id) {
		
		ColaboradorDTO colaboradorDTO = colaboradorService.findPessoaFisicaById(id);

		return ResponseEntity.ok(colaboradorDTO);
	}
	// Buscar colaborador físico
	@GetMapping("/{id}")
	public ResponseEntity<ColaboradorDTO> findById(@PathVariable UUID id) {
		ColaboradorDTO colaboradorDTO = colaboradorService.findById(id);
		return ResponseEntity.ok(colaboradorDTO);
	}
	
	
	// Cadastrar colaborador físico
	@PostMapping
	public ResponseEntity<?> CreateColaborador(@RequestBody @Valid ColaboradorDTO dto) {

		colaboradorService.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	// Atualizar colaborador físico
	@PutMapping("/{id}")
	public ResponseEntity<?> upadateColaborador(@PathVariable UUID id, @RequestBody @Valid ColaboradorDTO dto) {

		// Realiza a validação
		List<String> erros = colaboradorService.uniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		colaboradorService.update(id, dto);
		return ResponseEntity.ok("Colaborador atualizado com sucesso!");
	}
	// Excluir colaborador
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteColaboradorById(@PathVariable UUID id) {		
		colaboradorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os colaboradores
	@GetMapping
	public ResponseEntity<List<ColaboradorDTO>> findAll() {

		List<ColaboradorDTO> colaboradores = colaboradorService.findAll();
		return ResponseEntity.ok(colaboradores);
	}

	@PostMapping("/validar")
	public ResponseEntity<?> validateColaborador(@RequestBody @Valid ColaboradorDTO dto) {
		// Realiza a validação
		List<String> erros = colaboradorService.uniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = colaboradorService.existsById(dto.getId());
			if (cadastrado) {
				erros.add("Colaborador já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}
		return ResponseEntity.ok().build();
	}
}
