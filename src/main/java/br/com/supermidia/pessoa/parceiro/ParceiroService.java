package br.com.supermidia.pessoa.parceiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.supermidia.pessoa.dominio.Fisica;
import br.com.supermidia.pessoa.dominio.FisicaRepository;
import br.com.supermidia.pessoa.dominio.Juridica;
import br.com.supermidia.pessoa.dominio.JuridicaRepository;
import br.com.supermidia.pessoa.dominio.Pessoa;
import br.com.supermidia.pessoa.dominio.PessoaRepository;
import br.com.supermidia.pessoa.dominio.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class ParceiroService {
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private ParceiroRepository parceiroRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private FisicaRepository fisicaRepository;
	@Autowired
	private JuridicaRepository juridicaRepository;
	@Autowired
	private ParceiroMapper parceiroMapper;

	@Transactional
	public Parceiro saveFisico(ParceiroFisicoDTO dto) {
		Parceiro parceiro = parceiroMapper.toParceiroFisico(dto);
		if (dto.getId() != null) {
			Fisica fisica = fisicaRepository.findById(dto.getId()).get();
			parceiro.setPessoa(fisica);
			if (parceiroRepository.existsById(dto.getId())) {
				parceiro.setId(dto.getId());
			} else {
				parceiro.setId(null);
			}
		}
		return parceiroRepository.save(parceiro);
	}

	@Transactional
	public Parceiro saveJuridico(ParceiroJuridicoDTO dto) {
		Parceiro parceiro = parceiroMapper.toParceiroJuridico(dto);
		if (dto.getId() != null) {
			Juridica juridica = juridicaRepository.findById(dto.getId()).get();
			parceiro.setPessoa(juridica);
			if (parceiroRepository.existsById(dto.getId())) {
				parceiro.setId(dto.getId());
			} else {
				parceiro.setId(null);
			}
		}
		return parceiroRepository.save(parceiro);
	}

	@Transactional
	public void updateFisico(UUID id, ParceiroFisicoDTO parceiroFisicoDTO) {
		// Busca o parceiro existente
		Parceiro parceiro = parceiroRepository.findByPessoaId(parceiroFisicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado."));
		// Atualiza os dados do parceiro usando o mapper
		parceiroMapper.updateParceiroFisicoFromDTO(parceiroFisicoDTO, parceiro);
		// Salva o parceiro atualizado
		parceiroRepository.save(parceiro);
	}

	@Transactional
	public void updateJuridico(UUID id, ParceiroJuridicoDTO parceiroJuridicoDTO) {
		// Busca o parceiro existente
		Parceiro parceiro = parceiroRepository.findByPessoaId(parceiroJuridicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado."));
		// Atualiza os dados do parceiro usando o mapper
		parceiroMapper.updateParceiroJuridicoFromDTO(parceiroJuridicoDTO, parceiro);
		// Salva o parceiro atualizado
		parceiroRepository.save(parceiro);
	}

	@Transactional
	public void deleteById(UUID parceiroId) {
		Parceiro parceiro = parceiroRepository.findById(parceiroId)
				.orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado."));
		Fisica fisica;
		if ((parceiro.getPessoa()).getClass() == Fisica.class) {
			fisica = fisicaRepository.findById(parceiroId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada."));
			if (pessoaService.parceiroTemOutroPapel(fisica.getId())) {
				parceiroRepository.deleteById(parceiroId);
				return;
			} else {
				parceiroRepository.deleteById(parceiroId);
				fisicaRepository.deleteById(parceiroId);
				pessoaRepository.deleteById(parceiroId);
				return;
			}
		}
		Juridica juridica;
		if ((parceiro.getPessoa()).getClass() == Juridica.class) {
			juridica = juridicaRepository.findById(parceiroId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa jurídica não encontrada."));
			if (pessoaService.parceiroTemOutroPapel(juridica.getId())) {
				parceiroRepository.deleteById(parceiroId);

				return;
			} else {
				parceiroRepository.deleteById(parceiroId);
				juridicaRepository.deleteById(parceiroId);
				pessoaRepository.deleteById(parceiroId);
				return;
			}
		}
	}

	public List<ParceiroDTO> findAll() {
		List<Parceiro> parceiros = parceiroRepository.findAll();
		List<ParceiroDTO> parceirosDTO = new ArrayList<>();
		for (Parceiro parceiro : parceiros) {
			ParceiroDTO dto = new ParceiroDTO();
			dto.setId(parceiro.getId());
			dto.setNome(parceiro.getPessoa().getNome());
			dto.setEmail(parceiro.getPessoa().getEmail());
			dto.setTelefone(parceiro.getPessoa().getTelefone());
			dto.setMunicipio(parceiro.getPessoa().getMunicipio());
			dto.setUf(parceiro.getPessoa().getUf());
			dto.setTipo(parceiro.getPessoa().getTipo());
			parceirosDTO.add(dto);
		}
		return parceirosDTO;
	}

	@Transactional(readOnly = true)
	public ParceiroDTO findById(UUID id) {
		// Buscar na tabela de pessoas físicas
		Optional<Fisica> fisicaOptional = fisicaRepository.findById(id);
		if (fisicaOptional.isPresent()) {
			Fisica fisica = fisicaOptional.get();
			Parceiro parceiro = parceiroRepository.findByPessoaId(fisica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Parceiro associado não encontrado."));
			return parceiroMapper.toParceiroFisicoDTO(fisica, parceiro);
		}
		// Buscar na tabela de pessoas jurídicas
		Optional<Juridica> juridicaOptional = juridicaRepository.findById(id);
		if (juridicaOptional.isPresent()) {
			Juridica juridica = juridicaOptional.get();
			Parceiro parceiro = parceiroRepository.findByPessoaId(juridica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Parceiro associado não encontrado."));
			return parceiroMapper.toParceiroJuridicoDTO(juridica, parceiro);
		}
		// Se não encontrado em nenhum dos dois
		throw new IllegalArgumentException("Parceiro não encontrado.");
	}

	public ParceiroFisicoDTO findFisicoById(UUID id) {
		// Buscar o parceiro pelo ID
		Parceiro parceiro = parceiroRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Parceiro não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(parceiro.getPessoa() instanceof Fisica fisica)) {
			throw new IllegalStateException("A pessoa associada ao parceiro não é do tipo Fisica.");
		}
		// Mapear o parceiro e a entidade Fisica para ParceiroFisicoDTO
		return parceiroMapper.toParceiroFisicoDTO(fisica, parceiro);
	}

	public ParceiroJuridicoDTO findJuridicoById(UUID id) {
		// Buscar o parceiro pelo ID
		Parceiro parceiro = parceiroRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Parceiro não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(parceiro.getPessoa() instanceof Juridica juridica)) {
			throw new IllegalStateException("A pessoa associada ao parceiro não é do tipo Jurídica.");
		}
		// Mapear o parceiro e a entidade Fisica para ParceiroFisicoDTO
		return parceiroMapper.toParceiroJuridicoDTO(juridica, parceiro);
	}

	public List<String> fisicoUniqueAttributeValidation(ParceiroFisicoDTO parceiroFisicoDTO) {
		List<String> erros = new ArrayList<>();
		if (parceiroFisicoDTO.getId() == null) {
			uniquenessValidation(parceiroFisicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			uniquenessValidation(parceiroFisicoDTO.getEmail(), null, "email", valor -> pessoaRepository.existsByEmail(valor),
					erros);
			uniquenessValidation(parceiroFisicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			uniquenessValidation(parceiroFisicoDTO.getRg(), null, "rg", valor -> fisicaRepository.existsByRg(valor), erros);
			uniquenessValidation(parceiroFisicoDTO.getCpf(), null, "cpf", valor -> fisicaRepository.existsByCpf(valor),
					erros);
			return erros;
		} else {
			UUID id = parceiroFisicoDTO.getId();
			uniquenessValidation(parceiroFisicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroFisicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroFisicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroFisicoDTO.getRg(), id, "rg",
					valor -> fisicaRepository.existsByRgAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroFisicoDTO.getCpf(), id, "cpf",
					valor -> fisicaRepository.existsByCpfAndIdNot(valor, id), erros);
			return erros;
		}
	}

	public List<String> jurididicoUniqueAttributeValidation(@Valid ParceiroJuridicoDTO parceiroJuridicoDTO) {
		List<String> erros = new ArrayList<>();
		if (parceiroJuridicoDTO.getId() == null) {
			uniquenessValidation(parceiroJuridicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			uniquenessValidation(parceiroJuridicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			uniquenessValidation(parceiroJuridicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			uniquenessValidation(parceiroJuridicoDTO.getIe(), null, "ie", valor -> juridicaRepository.existsByIe(valor),
					erros);
			uniquenessValidation(parceiroJuridicoDTO.getCnpj(), null, "cnpj",
					valor -> juridicaRepository.existsByCnpj(valor), erros);
			return erros;
		} else {
			UUID id = parceiroJuridicoDTO.getId();
			uniquenessValidation(parceiroJuridicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroJuridicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroJuridicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroJuridicoDTO.getIe(), id, "rg",
					valor -> juridicaRepository.existsByIeAndIdNot(valor, id), erros);
			uniquenessValidation(parceiroJuridicoDTO.getCnpj(), id, "cpf",
					valor -> juridicaRepository.existsByCnpjAndIdNot(valor, id), erros);
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
		return parceiroRepository.existsById(id);
	}

	public ParceiroFisicoDTO findPessoaFisicaById(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"FÍSICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é Física.");
		}
		Fisica fisica = fisicaRepository.findById(id).get();
		Parceiro parceiro = new Parceiro();
		parceiro.setId(id);
		// Mapear para ParceiroFisicoDTO
		return parceiroMapper.toParceiroFisicoDTO(fisica, parceiro);
	}

	public ParceiroJuridicoDTO findPessoaJuridicaById(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"JURÍDICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é  Jurídica.");
		}
		Juridica juridica = juridicaRepository.findById(id).get();
		Parceiro parceiro = new Parceiro();
		parceiro.setId(id);
		// Mapear para ParceiroJuridicoDTO
		return parceiroMapper.toParceiroJuridicoDTO(juridica, parceiro);
	}
}
