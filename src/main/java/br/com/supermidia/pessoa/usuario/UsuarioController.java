package br.com.supermidia.pessoa.usuario;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

	// Endpoint para cadastro de um novo usuário
	@PostMapping
	public ResponseEntity<Usuario> inserir(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioService.create(usuarioDTO);
		return ResponseEntity.ok(usuario);
	}

	// Endpoint para editação de um usuário
	@PutMapping
	public ResponseEntity<Usuario> editarPermissoes(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuario = usuarioService.update(usuarioDTO.getId(), usuarioDTO.getPermissoes());
		return ResponseEntity.ok(usuario);
	}

	// Endpoint para listar todos os usuários
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listar() {
		List<UsuarioDTO> usuarios = usuarioService.dtoFindAll();
		return ResponseEntity.ok(usuarios);
	}

	// Endpoint para buscar usuário por ID
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
		UsuarioDTO usuarioDTO = usuarioService.findById(id); // Chama o serviço
		return ResponseEntity.ok(usuarioDTO); // Retorna o DTO
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<UsuarioDTO> findByEmail(@PathVariable String email) {
		UsuarioDTO usuarioDTO = usuarioService.findByEmail(email); // Chama o serviço
		return ResponseEntity.ok(usuarioDTO); // Retorna o DTO
	}

	// Endpoint para excluir usuário por ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirPorId(@PathVariable UUID id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Endpoint para validar o token
	@GetMapping("/validate-token")
	public ResponseEntity<?> validateToken() {
		try {
			// Verifica se o token é válido
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			// Retorna uma mensagem de sucesso com o usuário autenticado
			return ResponseEntity.ok(Map.of("message", "Token válido", "user", userDetails.getUsername()));
		} catch (Exception e) {
			// Retorna erro se o token for inválido
			return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
		}
	}
}
