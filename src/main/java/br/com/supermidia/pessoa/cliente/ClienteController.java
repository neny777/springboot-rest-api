package br.com.supermidia.pessoa.cliente;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	// Cadastrar cliente físico
	@PostMapping("/fisico")
	public ResponseEntity<Cliente> cadastrarClienteFisico(@RequestBody @Valid ClienteFisicoDTO dto) {
		Cliente cliente = clienteService.saveFisico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
	}

	// Cadastrar cliente jurídico
	@PostMapping("/juridico")
	public ResponseEntity<Cliente> cadastrarClienteJuridico(@RequestBody @Valid ClienteJuridicoDTO dto) {
		System.out.println("Incio método saveJuridico Controller Service");
		Cliente cliente = clienteService.saveJuridico(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
	}

	// Atualizar cliente físico
	@PostMapping("/fisico/{id}")
	public ResponseEntity<Cliente> atualizarClienteFisico(@PathVariable UUID id,
			@RequestBody @Valid ClienteFisicoDTO dto) {
		Cliente clienteAtualizado = clienteService.updateFisico(id, dto);
		return ResponseEntity.ok(clienteAtualizado);
	}

	// Atualizar cliente jurídico
	@PostMapping("/juridico/{id}")
	public ResponseEntity<Cliente> atualizarClienteJuridico(@PathVariable UUID id,
			@RequestBody @Valid ClienteJuridicoDTO dto) {
		Cliente clienteAtualizado = clienteService.updateJuridico(id, dto);
		return ResponseEntity.ok(clienteAtualizado);
	}

	// Excluir cliente
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirCliente(@PathVariable UUID id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Listar todos os clientes
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> listarClientes() {
		System.out.println("Método listar clientes Controller");
		List<ClienteDTO> clientes = clienteService.listarTodosClientes();
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable UUID id) {
		ClienteDTO clienteDTO = clienteService.buscarPorId(id);
		return ResponseEntity.ok(clienteDTO);
	}
}
