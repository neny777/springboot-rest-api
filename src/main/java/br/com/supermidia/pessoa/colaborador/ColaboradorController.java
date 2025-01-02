package br.com.supermidia.pessoa.colaborador;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

	@Autowired
	private ColaboradorService colaboradorService;

	// Endpoint para buscar todos os colaboradores
	@GetMapping
	public ResponseEntity<List<ColaboradorDTO>> findAll() {
		List<ColaboradorDTO> colaboradores = colaboradorService.findAll();
		return ResponseEntity.ok(colaboradores);
	}

	// Endpoint para buscar um colaborador por ID
	@GetMapping("/{id}")
	public ResponseEntity<ColaboradorDTO> findById(@PathVariable UUID id) {
		ColaboradorDTO colaborador = colaboradorService.dtoFindById(id);
		return ResponseEntity.ok(colaborador);
	}
	
	@GetMapping("/nao-usuario")
	public ResponseEntity<List<ColaboradorDTO>> findColaboradoresSemUsuario() {
	    List<ColaboradorDTO> colaboradoresNaoUsuario = colaboradorService.dtoFindAllNotUser();
	    return ResponseEntity.ok(colaboradoresNaoUsuario);
	}

	// Endpoint para criar um novo colaborador
	@PostMapping
	public ResponseEntity<ColaboradorDTO> create(@Validated @RequestBody ColaboradorDTO colaboradorDTO) {
		Colaborador colaborador = colaboradorService.save(colaboradorDTO, null);
		ColaboradorDTO createdColaborador = colaboradorService.dtoFindById(colaborador.getId());
		return ResponseEntity.ok(createdColaborador);
	}

	// Endpoint para atualizar um colaborador existente
	@PutMapping("/{id}")
	public ResponseEntity<ColaboradorDTO> update(@PathVariable UUID id,
			@Validated @RequestBody ColaboradorDTO colaboradorDTO) {
		Colaborador colaboradorExistente = colaboradorService.findById(id);
		Colaborador colaboradorAtualizado = colaboradorService.update(colaboradorExistente, colaboradorDTO);
		ColaboradorDTO updatedColaborador = colaboradorService.dtoFindById(colaboradorAtualizado.getId());
		return ResponseEntity.ok(updatedColaborador);
	}

	// Endpoint para deletar um colaborador
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		colaboradorService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/validate/{campo}")
	public ResponseEntity<Map<String, Boolean>> validarUnico(@PathVariable String campo, @RequestParam String valor,
			@RequestParam(required = false) UUID id) {

		boolean isUnique;

		try {
			colaboradorService.validarAtributoUnico(campo, valor, id);
			isUnique = true; // Não lançou exceção, então é único
		} catch (IllegalArgumentException e) {
			isUnique = false;
		}

		return ResponseEntity.ok(Map.of("unico", isUnique));
	}

}
