package br.com.supermidia.pessoa.cliente;

import java.time.LocalDate;
import java.util.UUID;

import br.com.supermidia.pessoa.dominio.Fisica;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ColaboradorDTO {
	private UUID id;
	
	@NotNull(message = "A descrição da função é obrigatória.")
	@Size(max = 100, message = "A descrição da função deve ter no máximo 100 caracteres.")
	private String funcao;

	@NotNull(message = "O número da carteira de trabalho é obrigatório.")
	@Size(max = 15, message = "O número da carteira de trabalho deve ter no máximo 15 caracteres.")
	private String ctps;
	
	@NotNull(message = "O valor do salário é obrigatório.")
	private Double salario;

	// Campos herdados de Fisica (que por sua vez herda de Pessoa)
	@NotNull(message = "O nome é obrigatório.")
	@Size(max = 60, message = "O nome deve ter no máximo 60 caracteres.")
	private String nome;

	@Email(message = "O e-mail deve ser válido.")
	@NotNull(message = "O e-mail é obrigatório.")
	@Size(max = 70, message = "O e-mail deve ter no máximo 70 caracteres.")
	private String email;

	@NotNull(message = "O tefelfone é obrigatório.")
	@Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
	private String telefone;

	@NotNull(message = "O CEP é obrigatório.")
	@Size(max = 9, message = "O cep deve ter no máximo 9 caracteres.")
	private String cep;
	
	@NotNull(message = "O logradouro é obrigatório.")
	@Size(max = 60, message = "O logradouro deve ter no máximo 60 caracteres.")
	private String logradouro;
	
	@Size(max = 6, message = "O número deve ter no máximo 6 caracteres.")
	private String numero;
	
	@NotNull(message = "O bairro é obrigatório.")
	@Size(max = 60, message = "O bairro deve ter no máximo 60 caracteres.")
	private String bairro;
	
	@NotNull(message = "O município é obrigatório.")
	@Size(max = 60, message = "O município deve ter no máximo 60 caracteres.")
	private String municipio;
	
	@Size(max = 2, message = "A unidade federal deve ter no máximo 2 caracteres.")
	private String uf;

	@NotNull(message = "O CPF é obrigatório.")
	@Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres.")
	private String cpf;

	@NotNull(message = "O RG é obrigatório.")
	@Size(max = 14, message = "O RG deve ter no máximo 14 caracteres.")
	private String rg;

	@NotNull(message = "O sexo é obrigatório.")
	private Fisica.Sexo sexo;

	@NotNull(message = "A data de nascimento é obrigatória.")
	private LocalDate dataNascimento;

	// Getters e Setters
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Fisica.Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Fisica.Sexo sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
