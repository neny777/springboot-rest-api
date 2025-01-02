package br.com.supermidia.security.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.supermidia.pessoa.usuario.Usuario;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Usuario usuario;

	public CustomUserDetails(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getPermissoes().stream().map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
				.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		if (usuario.getColaborador() != null && usuario.getColaborador().getFisica() != null) {
			return usuario.getColaborador().getFisica().getEmail();
		}
		throw new IllegalStateException("Usuário não possui colaborador ou fisica associado.");
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Adapte conforme necessário
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Adapte conforme necessário
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Adapte conforme necessário
	}

	@Override
	public boolean isEnabled() {
		return true; // Adapte conforme necessário
	}
}
