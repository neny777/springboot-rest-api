package br.com.supermidia.pessoa.colaborador;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.supermidia.pessoa.dominio.Fisica;
import br.com.supermidia.pessoa.dominio.FisicaRepository;
import br.com.supermidia.pessoa.dominio.Pessoa;
import br.com.supermidia.pessoa.dominio.PessoaRepository;
import br.com.supermidia.pessoa.dominio.PessoaService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ColaboradorService {
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private ColaboradorRepository colaboradorRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private FisicaRepository fisicaRepository;
	@Autowired
	private ColaboradorMapper colaboradorMapper;

	@Transactional
	public Colaborador save(ColaboradorDTO dto) {
		Colaborador colaborador = colaboradorMapper.toColaborador(dto);
		if (dto.getId() != null) {
			Fisica fisica = fisicaRepository.findById(dto.getId()).get();
			colaborador.setFisica(fisica);
			if (colaboradorRepository.existsById(dto.getId())) {
				colaborador.setId(dto.getId());
			} else {
				colaborador.setId(null);
			}
		}
		return colaboradorRepository.save(colaborador);
	}

	@Transactional
	public void update(UUID id, ColaboradorDTO colaboradorDTO) {
		// Busca o colaborador existente
		Colaborador colaborador = colaboradorRepository.findById(colaboradorDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Colaborador não encontrado."));
		// Atualiza os dados do colaborador usando o mapper
		colaboradorMapper.updateColaboradorFromDTO(colaboradorDTO, colaborador);
		// Salva o colaborador atualizado
		colaboradorRepository.save(colaborador);
	}

	@Transactional
	public void deleteById(UUID colaboradorId) {
		Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
				.orElseThrow(() -> new IllegalArgumentException("Colaborador não encontrado."));
		Fisica fisica = fisicaRepository.findById(colaborador.getId())
				.orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada."));
		if (pessoaService.colaboradorTemOutroPapel(fisica.getId())) {
			colaboradorRepository.deleteById(colaborador.getId());
			return;
		} else {
			colaboradorRepository.deleteById(colaborador.getId());
			fisicaRepository.deleteById(colaborador.getId());
			pessoaRepository.deleteById(colaborador.getId());
			return;
		}
	}

	public List<ColaboradorDTO> findAll() {
		List<Colaborador> colaboradores = colaboradorRepository.findAll();
		List<ColaboradorDTO> colaboradoresDTO = new ArrayList<>();
		for (Colaborador colaborador : colaboradores) {
			ColaboradorDTO dto = new ColaboradorDTO();
			dto.setId(colaborador.getId());
			dto.setNome(colaborador.getFisica().getNome());
			dto.setEmail(colaborador.getFisica().getEmail());
			dto.setTelefone(colaborador.getFisica().getTelefone());
			dto.setMunicipio(colaborador.getFisica().getMunicipio());
			dto.setUf(colaborador.getFisica().getUf());
			colaboradoresDTO.add(dto);
		}
		return colaboradoresDTO;
	}

	public ColaboradorDTO findById(UUID id) {
		// Buscar o colaborador pelo ID
		Colaborador colaborador = colaboradorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
		// Mapear o colaborador e a entidade Fisica para ColaboradorDTO
		return colaboradorMapper.toColaboradorDTO(colaborador.getFisica(), colaborador);
	}

	public List<String> uniqueAttributeValidation(ColaboradorDTO colaboradorDTO) {
		List<String> erros = new ArrayList<>();
		if (colaboradorDTO.getId() == null) {
			uniquenessValidation(colaboradorDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			uniquenessValidation(colaboradorDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			uniquenessValidation(colaboradorDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			uniquenessValidation(colaboradorDTO.getRg(), null, "rg", valor -> fisicaRepository.existsByRg(valor),
					erros);
			uniquenessValidation(colaboradorDTO.getCpf(), null, "cpf", valor -> fisicaRepository.existsByCpf(valor),
					erros);
			return erros;
		} else {
			UUID id = colaboradorDTO.getId();
			uniquenessValidation(colaboradorDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			uniquenessValidation(colaboradorDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			uniquenessValidation(colaboradorDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			uniquenessValidation(colaboradorDTO.getRg(), id, "rg",
					valor -> fisicaRepository.existsByRgAndIdNot(valor, id), erros);
			uniquenessValidation(colaboradorDTO.getCpf(), id, "cpf",
					valor -> fisicaRepository.existsByCpfAndIdNot(valor, id), erros);
			return erros;
		}
	}

	private void uniquenessValidation(String valor, UUID id, String campo, Function<String, Boolean> verificaUnicidade,
			List<String> erros) {
		if (valor == null || valor.isBlank()) {
			return; // Ignora valores nulos ou vazios
		}
		// Verifica se o valor já existe no banco
		boolean duplicado = verificaUnicidade.apply(valor);
		// Adiciona mensagem de erro à lista, em vez de lançar exceção
		if (duplicado) {
			erros.add(campo.toUpperCase() + " " + valor + " já está cadastrado");
		}
	}

	public boolean existsById(UUID id) {
		return colaboradorRepository.existsById(id);
	}

	public ColaboradorDTO findPessoaFisicaById(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"FÍSICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é Física.");
		}
		Fisica fisica = fisicaRepository.findById(id).get();
		Colaborador colaborador = new Colaborador();
		colaborador.setId(id);
		// Mapear para ColaboradorDTO
		return colaboradorMapper.toColaboradorDTO(fisica, colaborador);
	}
}
