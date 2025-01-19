package br.com.supermidia.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.supermidia.pessoa.usuario.Usuario;

@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;

	private final AuthenticationService authenticationService;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	/*
	 * @PostMapping("/signup") public ResponseEntity<User> register(@RequestBody
	 * RegisterUserDto registerUserDto) { User registeredUser =
	 * authenticationService.signup(registerUserDto);
	 * 
	 * return ResponseEntity.ok(registeredUser); }
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDto loginUserDto) {
		Usuario authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponseDTO loginResponse = new LoginResponseDTO().setToken(jwtToken)
				.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
	    // Qualquer lógica adicional, como logs de auditoria, pode ser inserida aqui
	    return ResponseEntity.noContent().build();
	}	
}