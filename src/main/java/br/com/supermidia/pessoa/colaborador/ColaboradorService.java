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
		colaboradorMapper.updateEntityFromDto(colaboradorDTO, colaboradorExistente);
		return colaboradorRepository.save(colaboradorExistente);
	}

	// Método para salvar um ColaboradorDTO (conversão do DTO para entity)
	public Colaborador saveColaborador(ColaboradorDTO colaboradorDTO) {

		validarNomeUnico(colaboradorDTO.getNome());
		validarEmailUnico(colaboradorDTO.getEmail());
		validarTelefoneUnico(colaboradorDTO.getTelefone());
		validarRgUnico(colaboradorDTO.getRg());
		validarCpfUnico(colaboradorDTO.getCpf());
		validarCtpsUnico(colaboradorDTO.getCtps());

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

	private void validarNomeUnico(String nome) {
		if (colaboradorRepository.existsByFisicaNome(nome)) {
			throw new IllegalArgumentException("Nome já cadastrado: " + nome);
		}
	}

	private void validarEmailUnico(String email) {
		if (colaboradorRepository.existsByFisicaEmail(email)) {
			throw new IllegalArgumentException("O email já cadastrado: " + email);
		}
	}

	private void validarTelefoneUnico(String telefone) {
		if (colaboradorRepository.existsByFisicaTelefone(telefone)) {
			throw new IllegalArgumentException("Telefone já cadastrado: " + telefone);
		}
	}

	private void validarRgUnico(String rg) {
		if (colaboradorRepository.existsByFisicaRg(rg)) {
			throw new IllegalArgumentException("RG já cadastrado: " + rg);
		}
	}

	private void validarCpfUnico(String cpf) {
		if (colaboradorRepository.existsByFisicaCpf(cpf)) {
			throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
		}
	}

	private void validarCtpsUnico(String ctps) {
		if (colaboradorRepository.existsByCtps(ctps)) {
			throw new IllegalArgumentException("CTPS já cadastrado: " + ctps);
		}
	}
}
