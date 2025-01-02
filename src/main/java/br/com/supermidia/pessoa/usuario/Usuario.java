package br.com.supermidia.pessoa.usuario;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.supermidia.pessoa.colaborador.Colaborador;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
@DynamicUpdate
public class Usuario {

	@Id
	private UUID id;

	@JsonIgnore
	@Column
	private String senha;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UsuarioPermissoes> permissoes = new HashSet<>();

	@OneToOne
	@MapsId
	@JoinColumn(name = "pessoa_id")
	private Colaborador colaborador;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Set<UsuarioPermissoes> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<UsuarioPermissoes> permissoes) {
		this.permissoes = permissoes;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
}