package br.com.supermidia.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.usuario.LoginUserDto;
import br.com.supermidia.pessoa.usuario.Usuario;
import br.com.supermidia.pessoa.usuario.UsuarioRepository;

@Service
public class AuthenticationService {
	private final UsuarioRepository usuarioRepository;

	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.usuarioRepository = usuarioRepository;
	}

	/*
	 * public User signup(RegisterUserDto input) { User user = new User()
	 * .setFullName(input.getFullName()) .setEmail(input.getEmail())
	 * .setPassword(passwordEncoder.encode(input.getPassword()));
	 * 
	 * return usuarioRepository.save(user); }
	 */
	public Usuario authenticate(LoginUserDto input) {
		System.out.println("Email: " + input.getEmail());
		System.out.println("Password: " + input.getPassword());
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return usuarioRepository.findByFisicaEmail(input.getEmail()).orElseThrow();
	}
}
