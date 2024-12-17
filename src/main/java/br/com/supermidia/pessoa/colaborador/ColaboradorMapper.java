package br.com.supermidia.pessoa.colaborador;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {

	@Mapping(target = "fisica.nome", source = "nome")
	@Mapping(target = "fisica.email", source = "email")
	@Mapping(target = "fisica.telefone", source = "telefone")
	@Mapping(target = "fisica.cep", source = "cep")
	@Mapping(target = "fisica.logradouro", source = "logradouro")
	@Mapping(target = "fisica.numero", source = "numero")
	@Mapping(target = "fisica.bairro", source = "bairro")
	@Mapping(target = "fisica.municipio", source = "municipio")
	@Mapping(target = "fisica.uf", source = "uf")
	@Mapping(target = "fisica.cpf", source = "cpf")
	@Mapping(target = "fisica.rg", source = "rg")
	@Mapping(target = "fisica.sexo", source = "sexo")
	@Mapping(target = "fisica.dataNascimento", source = "nascimento")
	Colaborador toEntity(ColaboradorDTO dto);
	
	
	@Mapping(target = "nome", source = "fisica.nome")
	@Mapping(target = "email", source = "fisica.email")
	@Mapping(target = "telefone", source = "fisica.telefone")
	@Mapping(target = "cep", source = "fisica.cep")
	@Mapping(target = "logradouro", source = "fisica.logradouro")
	@Mapping(target = "numero", source = "fisica.numero")
	@Mapping(target = "bairro", source = "fisica.bairro")
	@Mapping(target = "municipio", source = "fisica.municipio")
	@Mapping(target = "uf", source = "fisica.uf")
	@Mapping(target = "cpf", source = "fisica.cpf")
	@Mapping(target = "rg", source = "fisica.rg")
	@Mapping(target = "sexo", source = "fisica.sexo")
	@Mapping(target = "nascimento", source = "fisica.dataNascimento")
	ColaboradorDTO toDto(Colaborador entity);

	@Mapping(target = "fisica.nome", source = "nome")
	@Mapping(target = "fisica.email", source = "email")
	@Mapping(target = "fisica.telefone", source = "telefone")
	@Mapping(target = "fisica.cep", source = "cep")
	@Mapping(target = "fisica.logradouro", source = "logradouro")
	@Mapping(target = "fisica.numero", source = "numero")
	@Mapping(target = "fisica.bairro", source = "bairro")
	@Mapping(target = "fisica.municipio", source = "municipio")
	@Mapping(target = "fisica.uf", source = "uf")
	@Mapping(target = "fisica.cpf", source = "cpf")
	@Mapping(target = "fisica.rg", source = "rg")
	@Mapping(target = "fisica.sexo", source = "sexo")
	@Mapping(target = "fisica.dataNascimento", source = "nascimento")
	void updateEntityFromDto(ColaboradorDTO dto, @MappingTarget Colaborador entity);
}
