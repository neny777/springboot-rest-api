package br.com.supermidia.pessoa.cliente;

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
@RequestMapping("/api/clientes")
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	// Buscar pessoa física
	@GetMapping("/pessoa/fisica/{id}")
	public ResponseEntity<ClienteFisicoDTO> findPessoaFisicaById(@PathVariable UUID id) {

		ClienteFisicoDTO clienteFisicoDTO = clienteService.findPessoaFisicaById(id);

		return ResponseEntity.ok(clienteFisicoDTO);
	}

	// Buscar pessoa jurídica
	@GetMapping("/pessoa/juridica/{id}")
	public ResponseEntity<ClienteJuridicoDTO> findPessoaJuridicaById(@PathVariable UUID id) {

		ClienteJuridicoDTO clienteJuridicoDTO = clienteService.findPessoaJuridicaById(id);

		return ResponseEntity.ok(clienteJuridicoDTO);
	}

	// Buscar cliente físico
	@GetMapping("/fisico/{id}")
	public ResponseEntity<ClienteFisicoDTO> findClienteFisicoById(@PathVariable UUID id) {

		ClienteFisicoDTO clienteFisicoDTO = clienteService.findFisicoById(id);

		return ResponseEntity.ok(clienteFisicoDTO);
	}

	// Buscar cliente jurídico
	@GetMapping("/juridico/{id}")
	public ResponseEntity<ClienteJuridicoDTO> findClienteJuridicoById(@PathVariable UUID id) {

		ClienteJuridicoDTO clienteJuridicoDTO = clienteService.findJuridicoById(id);

		return ResponseEntity.ok(clienteJuridicoDTO);
	}

	// Cadastrar cliente físico
	@PostMapping("/fisico")
	public ResponseEntity<?> CreateClienteFisico(@RequestBody @Valid ClienteFisicoDTO dto) {

		clienteService.saveFisico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Cadastrar cliente jurídico
	@PostMapping("/juridico")
	public ResponseEntity<?> CreateClienteJuridico(@RequestBody @Valid ClienteJuridicoDTO dto) {

		clienteService.saveJuridico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Atualizar cliente físico
	@PutMapping("/fisico/{id}")
	public ResponseEntity<?> updateClienteFisico(@PathVariable UUID id, @RequestBody @Valid ClienteFisicoDTO dto) {

		// Realiza a validação
		List<String> erros = clienteService.fisicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		clienteService.updateFisico(id, dto);
		return ResponseEntity.ok("Cliente atualizado com sucesso!");
	}

	// Atualizar cliente jurídico
	@PutMapping("/juridico/{id}")
	public ResponseEntity<?> updateClienteJuridico(@PathVariable UUID id, @RequestBody @Valid ClienteJuridicoDTO dto) {

		// Realiza a validação
		List<String> erros = clienteService.jurididicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		clienteService.updateJuridico(id, dto);
		return ResponseEntity.ok("Cliente atualizado com sucesso!");
	}

	// Excluir cliente
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteClienteById(@PathVariable UUID id) {
		System.out.println("endpoint delete api clientes id");
		clienteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os clientes
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll() {

		List<ClienteDTO> clientes = clienteService.findAll();
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> findById(@PathVariable UUID id) {
		ClienteDTO clienteDTO = clienteService.findById(id);
		return ResponseEntity.ok(clienteDTO);
	}

	@PostMapping("/fisico/validar")
	public ResponseEntity<?> validateClienteFisico(@RequestBody @Valid ClienteFisicoDTO dto) {
		System.out.println("DTO");
		System.out.println(dto.getNome());
		// Realiza a validação
		List<String> erros = clienteService.fisicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			
			System.out.println("Erro(s): " + erros.size());
			for (String string : erros) {
				System.out.println(string);
			}
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = clienteService.existsById(dto.getId());
			if (cadastrado) {
				erros.add("Cliente já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/juridico/validar")
	public ResponseEntity<?> validateClienteJuridico(@RequestBody @Valid ClienteJuridicoDTO dto) {
		// Realiza a validação
		List<String> erros = clienteService.jurididicoUniqueAttributeValidation(dto);

		if (!erros.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("errors", erros));
		}

		if (dto.getId() != null) {
			boolean cadastrado = clienteService.existsById(dto.getId());
			if (cadastrado) {
				erros.add("Cliente já está cadastrado");
				return ResponseEntity.badRequest().body(Map.of("errors", erros));
			}
		}

		return ResponseEntity.ok().build();
	}
}
