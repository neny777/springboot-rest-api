package br.com.supermidia.pessoa.usuario;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.colaborador.Colaborador;
import br.com.supermidia.pessoa.colaborador.ColaboradorService;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ColaboradorService colaboradorService;

	@Autowired
	private UsuarioMapper usuarioMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Criar um novo usuário
	public Usuario create(UsuarioDTO usuarioDTO) {
		// Buscar colaborador existente
		Colaborador colaborador = colaboradorService.findById(usuarioDTO.getId());

		if (colaborador == null) {
			throw new IllegalArgumentException("Colaborador não encontrado para o ID fornecido.");
		}

		if (colaborador.getUsuario() != null) {
			throw new IllegalArgumentException("Este colaborador já possui um usuário associado.");
		}

		// Criar usuário
		Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
		usuario.setColaborador(colaborador);

		// Criptografar senha
		usuario.setSenha(passwordEncoder.encode("123"));

		// Persistir
		return usuarioRepository.save(usuario);
	}

	// Editar um novo usuário
	public Usuario update(UUID usuarioId, Set<String> novasPermissoes) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		// Remover permissões antigas
		usuario.getPermissoes().clear();

		// Adicionar novas permissões
		novasPermissoes.forEach(nomePermissao -> {
			UsuarioPermissoes permissao = new UsuarioPermissoes();
			permissao.setNome(nomePermissao);
			permissao.setUsuario(usuario);
			usuario.getPermissoes().add(permissao);
		});

		// Salvar alterações
		return usuarioRepository.save(usuario);
	}

	// Listar todos os usuários retornando DTOs
	public List<UsuarioDTO> dtoFindAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		return usuarios.stream().map(usuarioMapper::toResponse).toList(); // Converte para uma lista de UsuarioDTO
	}

	// Buscar um usuário por ID
	public UsuarioDTO findById(UUID id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
		return usuarioMapper.toResponse(usuario); // Converte para DTO
	}

	// Excluir um usuário.
	@Transactional
	public void deleteById(UUID usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

		// Desassocia o usuário do colaborador
		Colaborador colaborador = usuario.getColaborador();
		if (colaborador != null) {
			colaborador.setUsuario(null);
		}

		System.out.println("Excluindo permissões: " + usuario.getPermissoes());

		// Excluir o usuário e as permissões automaticamente
		usuarioRepository.delete(usuario);
		usuarioRepository.flush();

		System.out.println("Usuário excluído com sucesso.");
	}
}
