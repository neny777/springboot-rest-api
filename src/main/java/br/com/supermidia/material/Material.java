package br.com.supermidia.material;

import java.util.Objects;
import java.util.UUID;

import br.com.supermidia.converter.UppercaseConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "materiais", uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "marca"}))
public class Material {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@NotNull(message = "O nome é obrigatório.")
	@Size(max = 30, message = "O nome deve ter no máximo 30 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String nome;
	
	@NotNull(message = "A marca é obrigatória.")
	@Size(max = 30, message = "A marca deve ter no máximo 30 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String marca;
	
	@NotNull(message = "Tipo de unidade é obrigatório.")
	@Enumerated(EnumType.STRING)
	private Unidade unidade;
	
	@NotNull(message = "O preço é obrigatório.")
	private Double preco;	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public String getUnidadeDescricao() {
	    return unidade != null ? unidade.getDescricao() : "";
	}
	
	public enum Unidade {
		 un(""), m("m"), m2("m²"), l("l");

		private final String descricao;

		Unidade(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		return Objects.equals(id, other.id);
	}
}
