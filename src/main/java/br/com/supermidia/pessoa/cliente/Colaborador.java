package br.com.supermidia.pessoa.cliente;

import java.util.Objects;
import java.util.UUID;

import br.com.supermidia.pessoa.dominio.Fisica;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "colaboradores")
public class Colaborador {
	@Id
	private UUID id;
	@Size(max = 100, message = "A descrição função deve ter no máximo 100 caracteres.")
	private String funcao;
	@Size(max = 15, message = "O número da carteira de trabalho deve ter no máximo 15 caracteres.")	
	@Column(unique = true)
	private String ctps;
	private Double salario;
	@OneToOne
	@MapsId
	@JoinColumn(name = "pessoa_id")
	private Fisica fisica;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Fisica getFisica() {
		return fisica;
	}

	public void setFisica(Fisica fisica) {
		this.fisica = fisica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colaborador other = (Colaborador) obj;
		return Objects.equals(id, other.id);
	}
}
