package br.com.supermidia.pessoa.parceiro;

import java.util.UUID;

import br.com.supermidia.converter.LowercaseConverter;
import br.com.supermidia.converter.UppercaseConverter;
import br.com.supermidia.validation.CNPJ;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ParceiroJuridicoDTO extends ParceiroDTO {

	private UUID id;
	@NotNull(message = "O nome é obrigatório.")
	@Size(max = 60, message = "O nome deve ter no máximo 60 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String nome;
	@Email(message = "O e-mail deve ser válido.")
	@Size(max = 70, message = "O e-mail deve ter no máximo 70 caracteres.")
	@Convert(converter = LowercaseConverter.class)
	private String email;
	@Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
	private String telefone;
	@Size(max = 9, message = "O cep deve ter no máximo 9 caracteres.")
	@Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000.")
	@Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres.")
	private String cep;
	@Size(max = 60, message = "O logradouro deve ter no máximo 60 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String logradouro;
	@Size(max = 6, message = "O número deve ter no máximo 6 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String numero;
	@Size(max = 60, message = "O bairro deve ter no máximo 60 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String bairro;
	@Size(max = 60, message = "O município deve ter no máximo 60 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String municipio;
	@Size(max = 2, message = "A unidade federal deve ter no máximo 2 caracteres.")
	@Convert(converter = UppercaseConverter.class)
	private String uf;
	@CNPJ(message = "CNPJ inválido.")
	@Size(max = 18, message = "O cnpj deve ter no máximo 18 caracteres.")
	private String cnpj;
	@Size(max = 18, message = "A inscrição estadual deve ter no máximo 18 caracteres.")
	private String ie;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}
}
