package br.com.supermidia.pessoa.parceiro;

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
@RequestMapping("/api/parceiros")
public class ParceiroController {

	private final ParceiroService parceiroService;

	public ParceiroController(ParceiroService parceiroService) {
		this.parceiroService = parceiroService;
	}

	// Buscar pessoa física
	@GetMapping("/pessoa/fisica/{id}")
	public ResponseEntity<ParceiroFisicoDTO> findPessoaFisicaById(@PathVariable UUID id) {

		ParceiroFisicoDTO parceiroFisicoDTO = parceiroService.findPessoaFisicaById(id);

		return ResponseEntity.ok(parceiroFisicoDTO);
	}

	// Buscar pessoa jurídica
	@GetMapping("/pessoa/juridica/{id}")
	public ResponseEntity<ParceiroJuridicoDTO> findPessoaJuridicaById(@PathVariable UUID id) {

		ParceiroJuridicoDTO parceiroJuridicoDTO = parceiroService.findPessoaJuridicaById(id);

		return ResponseEntity.ok(parceiroJuridicoDTO);
	}

	// Buscar parceiro físico
	@GetMapping("/fisico/{id}")
	public ResponseEntity<ParceiroFisicoDTO> findParceiroFisicoById(@PathVariable UUID id) {

		ParceiroFisicoDTO parceiroFisicoDTO = parceiroService.findFisicoById(id);

		return ResponseEntity.ok(parceiroFisicoDTO);
	}

	// Buscar parceiro jurídico
	@GetMapping("/juridico/{id}")
	public ResponseEntity<ParceiroJuridicoDTO> findParceiroJuridicoById(@PathVariable UUID id) {

		ParceiroJuridicoDTO parceiroJuridicoDTO = parceiroService.findJuridicoById(id);

		return ResponseEntity.ok(parceiroJuridicoDTO);
	}

	// Cadastrar parceiro físico
	@PostMapping("/fisico")
	public ResponseEntity<?> CreateParceiroFisico(@RequestBody @Valid ParceiroFisicoDTO dto) {

		parceiroService.saveFisico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Cadastrar parceiro jurídico
	@PostMapping("/juridico")
	public ResponseEntity<?> CreateParceiroJuridico(@RequestBody @Valid ParceiroJuridicoDTO dto) {

		parceiroService.saveJuridico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Atualizar parceiro físico
	@PutMapping("/fisico/{id}")
	public ResponseEntity<?> updateParceiroFisico(@PathVariable UUID id, @RequestBody @Valid ParceiroFisicoDTO dto) {

		// Realiza a validação
		List<String> erros = parceiroService.fisicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		parceiroService.updateFisico(id, dto);
		return ResponseEntity.ok("Parceiro atualizado com sucesso!");
	}

	// Atualizar parceiro jurídico
	@PutMapping("/juridico/{id}")
	public ResponseEntity<?> upadateParceiroJuridico(@PathVariable UUID id, @RequestBody @Valid ParceiroJuridicoDTO dto) {

		// Realiza a validação
		List<String> erros = parceiroService.jurididicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		parceiroService.updateJuridico(id, dto);
		return ResponseEntity.ok("Parceiro atualizado com sucesso!");
	}

	// Excluir parceiro
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteParceiroById(@PathVariable UUID id) {
		System.out.println("endpoint delete api parceiros id");
		parceiroService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os parceiros
	@GetMapping
	public ResponseEntity<List<ParceiroDTO>> listarParceiros() {

		List<ParceiroDTO> parceiros = parceiroService.findAll();
		return ResponseEntity.ok(parceiros);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ParceiroDTO> findById(@PathVariable UUID id) {
		ParceiroDTO parceiroDTO = parceiroService.findById(id);
		return ResponseEntity.ok(parceiroDTO);
	}

	@PostMapping("/fisico/validar")
	public ResponseEntity<?> validateParceiroFisico(@RequestBody @Valid ParceiroFisicoDTO dto) {
		// Realiza a validação
		List<String> erros = parceiroService.fisicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = parceiroService.existsById(dto.getId());
			if (cadastrado) {
				erros.add("Parceiro já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/juridico/validar")
	public ResponseEntity<?> validateParceiroJuridico(@RequestBody @Valid ParceiroJuridicoDTO dto) {
		// Realiza a validação
		List<String> erros = parceiroService.jurididicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = parceiroService.existsById(dto.getId());
			if (cadastrado) {
				erros.add("Parceiro já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}
}
