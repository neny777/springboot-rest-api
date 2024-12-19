package br.com.supermidia.pessoa.colaborador;

import java.util.List;
import java.util.UUID;
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
		Colaborador colaborador = colaboradorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
		return colaboradorMapper.toDto(colaborador);
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

	private void validarNomeUnico(String nome, UUID id) {
		 if (nome == null) return;
		Colaborador existente = colaboradorRepository.getByFisicaNome(nome);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("Nome já cadastrado: " + nome);
		}
	}

	private void validarEmailUnico(String email, UUID id) {
		 if (email == null) return;
		Colaborador existente = colaboradorRepository.getByFisicaEmail(email);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("Email já cadastrado: " + email);
		}
	}

	private void validarTelefoneUnico(String telefone, UUID id) {
		 if (telefone == null) return;
		Colaborador existente = colaboradorRepository.getByFisicaTelefone(telefone);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("Telefone já cadastrado: " + telefone);
		}
	}

	private void validarRgUnico(String rg, UUID id) {
		 if (rg == null) return;
		Colaborador existente = colaboradorRepository.getByFisicaRg(rg);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("RG já cadastrado: " + rg);
		}
	}

	private void validarCpfUnico(String cpf, UUID id) {
		 if (cpf == null) return;
		Colaborador existente = colaboradorRepository.getByFisicaCpf(cpf);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
		}
	}

	private void validarCtpsUnico(String ctps, UUID id) {
		 if (ctps == null) return;
		Colaborador existente = colaboradorRepository.getByCtps(ctps);
		if (existente != null && (id == null || !existente.getId().equals(id))) {
			throw new IllegalArgumentException("CTPS já cadastrado: " + ctps);
		}
	}

	private void validarAtributosUnicos(ColaboradorDTO colaboradorDTO, UUID id) {
		validarNomeUnico(colaboradorDTO.getNome(), id);
		validarEmailUnico(colaboradorDTO.getEmail(), id);
		validarTelefoneUnico(colaboradorDTO.getTelefone(), id);
		validarRgUnico(colaboradorDTO.getRg(), id);
		validarCpfUnico(colaboradorDTO.getCpf(), id);
		validarCtpsUnico(colaboradorDTO.getCtps(), id);
	}
	
	public void validarAtributoUnico(String campo, String valor, UUID id) {
	    switch (campo) {
	        case "nome":
	            validarNomeUnico(valor, id);
	            break;
	        case "email":
	            validarEmailUnico(valor, id);
	            break;
	        case "telefone":
	            validarTelefoneUnico(valor, id);
	            break;
	        case "cpf":
	            validarCpfUnico(valor, id);
	            break;
	        case "rg":
	            validarRgUnico(valor, id);
	            break;
	        case "ctps":
	            validarCtpsUnico(valor, id);
	            break;
	        default:
	            throw new IllegalArgumentException("Campo inválido: " + campo);
	    }
	}
}
