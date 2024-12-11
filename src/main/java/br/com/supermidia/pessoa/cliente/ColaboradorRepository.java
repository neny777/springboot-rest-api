package br.com.supermidia.pessoa.cliente;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID> {

}
