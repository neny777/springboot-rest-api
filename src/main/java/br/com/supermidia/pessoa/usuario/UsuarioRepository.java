package br.com.supermidia.pessoa.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	@Query("SELECT u FROM Usuario u WHERE u.colaborador.fisica.email = :email")
	Optional<Usuario> findByFisicaEmail(@Param("email") String email);


}
