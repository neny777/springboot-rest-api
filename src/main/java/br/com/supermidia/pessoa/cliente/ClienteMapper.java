package br.com.supermidia.pessoa.cliente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.supermidia.pessoa.dominio.Fisica;
import br.com.supermidia.pessoa.dominio.Juridica;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

	// Mapear ClienteFisicoDTO para Fisica
	@Mapping(target = "id", ignore = true) // Ignorar o ID (gerado automaticamente)	
	Fisica toFisica(ClienteFisicoDTO clienteFisicoDTO);

	// Mapear ClienteJuridicoDTO para Juridica
	@Mapping(target = "id", ignore = true) // Ignorar o ID (gerado automaticamente)	
	Juridica toJuridica(ClienteJuridicoDTO clienteJuridicoDTO);

	// Mapear Fisica para ClienteFisicoDTO
	ClienteFisicoDTO toClienteFisicoDTO(Fisica fisica);

	// Mapear Juridica para ClienteJuridicoDTO
	ClienteJuridicoDTO toClienteJuridicoDTO(Juridica juridica);

	// Métodos de atualização
	@Mapping(target = "id", ignore = true)
	void updateFisicaFromDTO(ClienteFisicoDTO dto, @MappingTarget Fisica fisica);

	@Mapping(target = "id", ignore = true)	
	void updateJuridicaFromDTO(ClienteJuridicoDTO dto, @MappingTarget Juridica juridica);
	
	@Mapping(source = "cliente.id", target = "id")
    ClienteFisicoDTO toClienteFisicoDTO(Fisica fisica, Cliente cliente);

    @Mapping(source = "cliente.id", target = "id")
    ClienteJuridicoDTO toClienteJuridicoDTO(Juridica juridica, Cliente cliente);
}
