package br.com.supermidia.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.usuario.Usuario;
import br.com.supermidia.pessoa.usuario.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;	

	@Override
	public UserDetails loadUserByUsername(String identificacao) throws UsernameNotFoundException {
	    Usuario usuario = usuarioRepository.findByFisicaEmail(identificacao)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));	   

	    return new CustomUserDetails(usuario);
	}	
}
