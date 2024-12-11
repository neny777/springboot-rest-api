package br.com.supermidia.pessoa.cliente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {

	@Mapping(target = "fisica.cpf", source = "cpf")
	@Mapping(target = "fisica.rg", source = "rg")
	@Mapping(target = "fisica.sexo", source = "sexo")
	@Mapping(target = "fisica.dataNascimento", source = "dataNascimento")
	Colaborador toEntity(ColaboradorDTO dto);

	@Mapping(target = "cpf", source = "fisica.cpf")
	@Mapping(target = "rg", source = "fisica.rg")
	@Mapping(target = "sexo", source = "fisica.sexo")
	@Mapping(target = "dataNascimento", source = "fisica.dataNascimento")
	ColaboradorDTO toDto(Colaborador entity);

	@Mapping(target = "fisica.cpf", source = "cpf")
	@Mapping(target = "fisica.rg", source = "rg")
	@Mapping(target = "fisica.sexo", source = "sexo")
	@Mapping(target = "fisica.dataNascimento", source = "dataNascimento")
	void updateEntityFromDto(ColaboradorDTO dto, @MappingTarget Colaborador entity);
}
