package br.com.supermidia.pessoa.colaborador;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ColaboradorService {

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private ColaboradorMapper colaboradorMapper;

	// Método para encontrar todos os colaboradores
	public List<ColaboradorDTO> findAll() {
		List<Colaborador> colaboradores = colaboradorRepository.findAll();
		return colaboradores.stream().map(colaboradorMapper::toDto) // Usa o MapStruct para converter para DTO
				.collect(Collectors.toList());
	}

	// Método para encontrar um colaborador por ID e retornar um DTO
	public ColaboradorDTO findById(UUID id) {
		return colaboradorRepository.findById(id).map(colaboradorMapper::toDto)
				.orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
	}

	// Buscar a entity Colaborador por ID (para fins de atualização)
	public Colaborador findEntityById(UUID id) {
		return colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
	}

	// Atualizar o colaborador existente com os dados do ColaboradorDTO
	public Colaborador updateColaborador(Colaborador colaboradorExistente, ColaboradorDTO colaboradorDTO) {
		validarAtributosUnicos(colaboradorDTO, colaboradorExistente.getId());
		colaboradorMapper.updateEntityFromDto(colaboradorDTO, colaboradorExistente);
		return colaboradorRepository.save(colaboradorExistente);
	}

	// Método para salvar um ColaboradorDTO (conversão do DTO para entity)
	public Colaborador saveColaborador(ColaboradorDTO colaboradorDTO, UUID id) {

		validarAtributosUnicos(colaboradorDTO, id);

		try {
			Colaborador colaborador = colaboradorMapper.toEntity(colaboradorDTO);
			return colaboradorRepository.save(colaborador);
		} catch (DataIntegrityViolationException ex) {
			throw new IllegalArgumentException("Erro ao salvar: campo duplicado ou dados inválidos.", ex);
		}
	}

	// Método para deletar um colaborador por ID
	public void delete(UUID id) {
		colaboradorRepository.deleteById(id);
	}

	private void validarAtributosUnicos(ColaboradorDTO colaboradorDTO, UUID id) {
		validarUnicidade(colaboradorDTO.getNome(), id, "Nome", colaboradorRepository::getByFisicaNome);
		validarUnicidade(colaboradorDTO.getEmail(), id, "Email", colaboradorRepository::getByFisicaEmail);
		validarUnicidade(colaboradorDTO.getTelefone(), id, "Telefone", colaboradorRepository::getByFisicaTelefone);
		validarUnicidade(colaboradorDTO.getRg(), id, "RG", colaboradorRepository::getByFisicaRg);
		validarUnicidade(colaboradorDTO.getCpf(), id, "CPF", colaboradorRepository::getByFisicaCpf);
		validarUnicidade(colaboradorDTO.getCtps(), id, "CTPS", colaboradorRepository::getByCtps);
	}

	private void validarUnicidade(String valor, UUID id, String campo, Function<String, Colaborador> buscaPorCampo) {
		if (valor == null)
			return;
		Colaborador existente = buscaPorCampo.apply(valor);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			lançarExceçãoDuplicado(campo, valor);
		}
	}
	
	public void validarAtributoUnico(String campo, String valor, UUID id) {
	    if (valor == null || valor.isBlank()) {
	        throw new IllegalArgumentException("O valor para validação não pode ser nulo ou vazio.");
	    }

	    switch (campo.toLowerCase()) {
	        case "nome":
	            validarUnicidade(valor, id, "Nome", colaboradorRepository::getByFisicaNome);
	            break;
	        case "email":
	            validarUnicidade(valor, id, "Email", colaboradorRepository::getByFisicaEmail);
	            break;
	        case "telefone":
	            validarUnicidade(valor, id, "Telefone", colaboradorRepository::getByFisicaTelefone);
	            break;
	        case "cpf":
	            validarUnicidade(valor, id, "CPF", colaboradorRepository::getByFisicaCpf);
	            break;
	        case "rg":
	            validarUnicidade(valor, id, "RG", colaboradorRepository::getByFisicaRg);
	            break;
	        case "ctps":
	            validarUnicidade(valor, id, "CTPS", colaboradorRepository::getByCtps);
	            break;
	        default:
	            throw new IllegalArgumentException("Campo inválido para validação: " + campo);
	    }
	}

	private void lançarExceçãoDuplicado(String campo, String valor) {
		throw new IllegalArgumentException(campo + " já cadastrado: " + valor);
	}
}
