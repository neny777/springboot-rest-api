package br.com.supermidia.pessoa.colaborador;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID> {

}
