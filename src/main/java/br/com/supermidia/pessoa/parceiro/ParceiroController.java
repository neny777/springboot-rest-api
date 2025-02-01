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
	public ResponseEntity<ParceiroFisicoDTO> buscarPessoaFisica(@PathVariable UUID id) {

		ParceiroFisicoDTO parceiroFisicoDTO = parceiroService.buscarPessoaFisica(id);

		return ResponseEntity.ok(parceiroFisicoDTO);
	}

	// Buscar pessoa jurídica
	@GetMapping("/pessoa/juridica/{id}")
	public ResponseEntity<ParceiroJuridicoDTO> buscarPessoaJuridica(@PathVariable UUID id) {

		ParceiroJuridicoDTO parceiroJuridicoDTO = parceiroService.buscarPessoaJuridica(id);

		return ResponseEntity.ok(parceiroJuridicoDTO);
	}

	// Buscar parceiro físico
	@GetMapping("/fisico/{id}")
	public ResponseEntity<ParceiroFisicoDTO> buscarParceiroFisico(@PathVariable UUID id) {

		ParceiroFisicoDTO parceiroFisicoDTO = parceiroService.buscarParceiroFisico(id);

		return ResponseEntity.ok(parceiroFisicoDTO);
	}

	// Buscar parceiro jurídico
	@GetMapping("/juridico/{id}")
	public ResponseEntity<ParceiroJuridicoDTO> buscarParceiroJuridico(@PathVariable UUID id) {

		ParceiroJuridicoDTO parceiroJuridicoDTO = parceiroService.buscarParceiroJuridico(id);

		return ResponseEntity.ok(parceiroJuridicoDTO);
	}

	// Cadastrar parceiro físico
	@PostMapping("/fisico")
	public ResponseEntity<?> CadastrarParceiroFisico(@RequestBody @Valid ParceiroFisicoDTO dto) {

		parceiroService.saveFisico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Cadastrar parceiro jurídico
	@PostMapping("/juridico")
	public ResponseEntity<?> CadastrarParceiroJuridico(@RequestBody @Valid ParceiroJuridicoDTO dto) {

		parceiroService.saveJuridico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Atualizar parceiro físico
	@PutMapping("/fisico/{id}")
	public ResponseEntity<?> editarParceiroFisico(@PathVariable UUID id, @RequestBody @Valid ParceiroFisicoDTO dto) {

		// Realiza a validação
		List<String> erros = parceiroService.validarAtributosFisicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		parceiroService.editarParceiroFisico(id, dto);
		return ResponseEntity.ok("Parceiro atualizado com sucesso!");
	}

	// Atualizar parceiro jurídico
	@PutMapping("/juridico/{id}")
	public ResponseEntity<?> editarParceiroJuridico(@PathVariable UUID id, @RequestBody @Valid ParceiroJuridicoDTO dto) {

		// Realiza a validação
		List<String> erros = parceiroService.validarAtributosJuridicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		parceiroService.editarParceiroJuridico(id, dto);
		return ResponseEntity.ok("Parceiro atualizado com sucesso!");
	}

	// Excluir parceiro
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirParceiro(@PathVariable UUID id) {
		System.out.println("endpoint delete api parceiros id");
		parceiroService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os parceiros
	@GetMapping
	public ResponseEntity<List<ParceiroDTO>> listarParceiros() {

		List<ParceiroDTO> parceiros = parceiroService.listarParceirosDTO();
		return ResponseEntity.ok(parceiros);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ParceiroDTO> buscarPorId(@PathVariable UUID id) {
		ParceiroDTO parceiroDTO = parceiroService.buscarPorId(id);
		return ResponseEntity.ok(parceiroDTO);
	}

	@PostMapping("/fisico/validar")
	public ResponseEntity<?> validarParceiroFisico(@RequestBody @Valid ParceiroFisicoDTO dto) {
		// Realiza a validação
		List<String> erros = parceiroService.validarAtributosFisicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = parceiroService.existeParceiroPorId(dto.getId());
			if (cadastrado) {
				erros.add("Parceiro já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/juridico/validar")
	public ResponseEntity<?> validarParceiroJuridico(@RequestBody @Valid ParceiroJuridicoDTO dto) {
		// Realiza a validação
		List<String> erros = parceiroService.validarAtributosJuridicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = parceiroService.existeParceiroPorId(dto.getId());
			if (cadastrado) {
				erros.add("Parceiro já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}
}
