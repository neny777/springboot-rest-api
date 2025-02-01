package br.com.supermidia.pessoa.fornecedor;

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
@RequestMapping("/api/fornecedores")
public class FornecedorController {

	private final FornecedorService fornecedorService;

	public FornecedorController(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	// Buscar pessoa física
	@GetMapping("/pessoa/fisica/{id}")
	public ResponseEntity<FornecedorFisicoDTO> buscarPessoaFisica(@PathVariable UUID id) {

		FornecedorFisicoDTO fornecedorFisicoDTO = fornecedorService.buscarPessoaFisica(id);

		return ResponseEntity.ok(fornecedorFisicoDTO);
	}

	// Buscar pessoa jurídica
	@GetMapping("/pessoa/juridica/{id}")
	public ResponseEntity<FornecedorJuridicoDTO> buscarPessoaJuridica(@PathVariable UUID id) {

		FornecedorJuridicoDTO fornecedorJuridicoDTO = fornecedorService.buscarPessoaJuridica(id);

		return ResponseEntity.ok(fornecedorJuridicoDTO);
	}

	// Buscar fornecedor físico
	@GetMapping("/fisico/{id}")
	public ResponseEntity<FornecedorFisicoDTO> buscarFornecedorFisico(@PathVariable UUID id) {

		FornecedorFisicoDTO fornecedorFisicoDTO = fornecedorService.buscarFornecedorFisico(id);

		return ResponseEntity.ok(fornecedorFisicoDTO);
	}

	// Buscar fornecedor jurídico
	@GetMapping("/juridico/{id}")
	public ResponseEntity<FornecedorJuridicoDTO> buscarFornecedorJuridico(@PathVariable UUID id) {

		FornecedorJuridicoDTO fornecedorJuridicoDTO = fornecedorService.buscarFornecedorJuridico(id);

		return ResponseEntity.ok(fornecedorJuridicoDTO);
	}

	// Cadastrar fornecedor físico
	@PostMapping("/fisico")
	public ResponseEntity<?> CadastrarFornecedorFisico(@RequestBody @Valid FornecedorFisicoDTO dto) {

		fornecedorService.saveFisico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Cadastrar fornecedor jurídico
	@PostMapping("/juridico")
	public ResponseEntity<?> CadastrarFornecedorJuridico(@RequestBody @Valid FornecedorJuridicoDTO dto) {

		fornecedorService.saveJuridico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Atualizar fornecedor físico
	@PutMapping("/fisico/{id}")
	public ResponseEntity<?> editarFornecedorFisico(@PathVariable UUID id, @RequestBody @Valid FornecedorFisicoDTO dto) {

		// Realiza a validação
		List<String> erros = fornecedorService.validarAtributosFisicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		fornecedorService.editarFornecedorFisico(id, dto);
		return ResponseEntity.ok("Fornecedor atualizado com sucesso!");
	}

	// Atualizar fornecedor jurídico
	@PutMapping("/juridico/{id}")
	public ResponseEntity<?> editarFornecedorJuridico(@PathVariable UUID id, @RequestBody @Valid FornecedorJuridicoDTO dto) {

		// Realiza a validação
		List<String> erros = fornecedorService.validarAtributosJuridicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		fornecedorService.editarFornecedorJuridico(id, dto);
		return ResponseEntity.ok("Fornecedor atualizado com sucesso!");
	}

	// Excluir fornecedor
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirFornecedor(@PathVariable UUID id) {
		System.out.println("endpoint delete api fornecedores id");
		fornecedorService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os fornecedores
	@GetMapping
	public ResponseEntity<List<FornecedorDTO>> listarFornecedores() {

		List<FornecedorDTO> fornecedores = fornecedorService.listarFornecedoresDTO();
		return ResponseEntity.ok(fornecedores);
	}

	@GetMapping("/{id}")
	public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable UUID id) {
		FornecedorDTO fornecedorDTO = fornecedorService.buscarPorId(id);
		return ResponseEntity.ok(fornecedorDTO);
	}

	@PostMapping("/fisico/validar")
	public ResponseEntity<?> validarFornecedorFisico(@RequestBody @Valid FornecedorFisicoDTO dto) {
		// Realiza a validação
		List<String> erros = fornecedorService.validarAtributosFisicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = fornecedorService.existeFornecedorPorId(dto.getId());
			if (cadastrado) {
				erros.add("Fornecedor já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/juridico/validar")
	public ResponseEntity<?> validarFornecedorJuridico(@RequestBody @Valid FornecedorJuridicoDTO dto) {
		// Realiza a validação
		List<String> erros = fornecedorService.validarAtributosJuridicoUnicos(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = fornecedorService.existeFornecedorPorId(dto.getId());
			if (cadastrado) {
				erros.add("Fornecedor já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}
}
