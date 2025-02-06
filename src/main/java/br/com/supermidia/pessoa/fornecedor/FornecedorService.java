package br.com.supermidia.pessoa.fornecedor;

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
public class FornecedorService {
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private FornecedorRepository fornecedorRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private FisicaRepository fisicaRepository;
	@Autowired
	private JuridicaRepository juridicaRepository;
	@Autowired
	private FornecedorMapper fornecedorMapper;

	@Transactional
	public Fornecedor saveFisico(FornecedorFisicoDTO dto) {
		Fornecedor fornecedor = fornecedorMapper.toFornecedorFisico(dto);
		if (dto.getId() != null) {
			Fisica fisica = fisicaRepository.findById(dto.getId()).get();
			fornecedor.setPessoa(fisica);
			if (fornecedorRepository.existsById(dto.getId())) {
				fornecedor.setId(dto.getId());
			} else {
				fornecedor.setId(null);
			}
		}
		return fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public Fornecedor saveJuridico(FornecedorJuridicoDTO dto) {
		Fornecedor fornecedor = fornecedorMapper.toFornecedorJuridico(dto);
		if (dto.getId() != null) {
			Juridica juridica = juridicaRepository.findById(dto.getId()).get();
			fornecedor.setPessoa(juridica);
			if (fornecedorRepository.existsById(dto.getId())) {
				fornecedor.setId(dto.getId());
			} else {
				fornecedor.setId(null);
			}
		}
		return fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public void updateFisico(UUID id, FornecedorFisicoDTO fornecedorFisicoDTO) {
		// Busca o fornecedor existente
		Fornecedor fornecedor = fornecedorRepository.findByPessoaId(fornecedorFisicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));
		// Atualiza os dados do fornecedor usando o mapper
		fornecedorMapper.updateFornecedorFisicoFromDTO(fornecedorFisicoDTO, fornecedor);
		// Salva o fornecedor atualizado
		fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public void updateJuridico(UUID id, FornecedorJuridicoDTO fornecedorJuridicoDTO) {
		// Busca o fornecedor existente
		Fornecedor fornecedor = fornecedorRepository.findByPessoaId(fornecedorJuridicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));
		// Atualiza os dados do fornecedor usando o mapper
		fornecedorMapper.updateFornecedorJuridicoFromDTO(fornecedorJuridicoDTO, fornecedor);
		// Salva o fornecedor atualizado
		fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public void deleteById(UUID fornecedorId) {
		Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));
		Fisica fisica;
		if ((fornecedor.getPessoa()).getClass() == Fisica.class) {
			fisica = fisicaRepository.findById(fornecedorId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada."));
			if (pessoaService.fornecedorTemOutroPapel(fisica.getId())) {
				fornecedorRepository.deleteById(fornecedorId);
				return;
			} else {
				fornecedorRepository.deleteById(fornecedorId);
				fisicaRepository.deleteById(fornecedorId);
				pessoaRepository.deleteById(fornecedorId);
				return;
			}
		}
		Juridica juridica;
		if ((fornecedor.getPessoa()).getClass() == Juridica.class) {
			juridica = juridicaRepository.findById(fornecedorId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa jurídica não encontrada."));
			if (pessoaService.fornecedorTemOutroPapel(juridica.getId())) {
				fornecedorRepository.deleteById(fornecedorId);
				return;
			} else {
				fornecedorRepository.deleteById(fornecedorId);
				juridicaRepository.deleteById(fornecedorId);
				pessoaRepository.deleteById(fornecedorId);
				return;
			}
		}
	}

	public List<FornecedorDTO> findAll() {
		List<Fornecedor> fornecedores = fornecedorRepository.findAll();
		List<FornecedorDTO> fornecedoresDTO = new ArrayList<>();
		for (Fornecedor fornecedor : fornecedores) {
			FornecedorDTO dto = new FornecedorDTO();
			dto.setId(fornecedor.getId());
			dto.setNome(fornecedor.getPessoa().getNome());
			dto.setEmail(fornecedor.getPessoa().getEmail());
			dto.setTelefone(fornecedor.getPessoa().getTelefone());
			dto.setMunicipio(fornecedor.getPessoa().getMunicipio());
			dto.setUf(fornecedor.getPessoa().getUf());
			dto.setTipo(fornecedor.getPessoa().getTipo());
			fornecedoresDTO.add(dto);
		}
		return fornecedoresDTO;
	}

	@Transactional(readOnly = true)
	public FornecedorDTO findById(UUID id) {
		// Buscar na tabela de pessoas físicas
		Optional<Fisica> fisicaOptional = fisicaRepository.findById(id);
		if (fisicaOptional.isPresent()) {
			Fisica fisica = fisicaOptional.get();
			Fornecedor fornecedor = fornecedorRepository.findByPessoaId(fisica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Fornecedor associado não encontrado."));
			return fornecedorMapper.toFornecedorFisicoDTO(fisica, fornecedor);
		}
		// Buscar na tabela de pessoas jurídicas
		Optional<Juridica> juridicaOptional = juridicaRepository.findById(id);
		if (juridicaOptional.isPresent()) {
			Juridica juridica = juridicaOptional.get();
			Fornecedor fornecedor = fornecedorRepository.findByPessoaId(juridica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Fornecedor associado não encontrado."));
			return fornecedorMapper.toFornecedorJuridicoDTO(juridica, fornecedor);
		}
		// Se não encontrado em nenhum dos dois
		throw new IllegalArgumentException("Fornecedor não encontrado.");
	}

	public FornecedorFisicoDTO findFisicoById(UUID id) {
		// Buscar o fornecedor pelo ID
		Fornecedor fornecedor = fornecedorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(fornecedor.getPessoa() instanceof Fisica fisica)) {
			throw new IllegalStateException("A pessoa associada ao fornecedor não é do tipo Fisica.");
		}
		// Mapear o fornecedor e a entidade Fisica para FornecedorFisicoDTO
		return fornecedorMapper.toFornecedorFisicoDTO(fisica, fornecedor);
	}

	public FornecedorJuridicoDTO findJuridicoById(UUID id) {
		// Buscar o fornecedor pelo ID
		Fornecedor fornecedor = fornecedorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(fornecedor.getPessoa() instanceof Juridica juridica)) {
			throw new IllegalStateException("A pessoa associada ao fornecedor não é do tipo Jurídica.");
		}
		// Mapear o fornecedor e a entidade Fisica para FornecedorFisicoDTO
		return fornecedorMapper.toFornecedorJuridicoDTO(juridica, fornecedor);
	}

	public List<String> fisicoUniqueAttributeValidation(FornecedorFisicoDTO fornecedorFisicoDTO) {
		List<String> erros = new ArrayList<>();
		if (fornecedorFisicoDTO.getId() == null) {
			uniquenessValidation(fornecedorFisicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			uniquenessValidation(fornecedorFisicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			uniquenessValidation(fornecedorFisicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			uniquenessValidation(fornecedorFisicoDTO.getRg(), null, "rg", valor -> fisicaRepository.existsByRg(valor),
					erros);
			uniquenessValidation(fornecedorFisicoDTO.getCpf(), null, "cpf", valor -> fisicaRepository.existsByCpf(valor),
					erros);
			return erros;
		} else {
			UUID id = fornecedorFisicoDTO.getId();
			uniquenessValidation(fornecedorFisicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorFisicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorFisicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorFisicoDTO.getRg(), id, "rg",
					valor -> fisicaRepository.existsByRgAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorFisicoDTO.getCpf(), id, "cpf",
					valor -> fisicaRepository.existsByCpfAndIdNot(valor, id), erros);
			return erros;
		}
	}

	public List<String> jurididicoUniqueAttributeValidation(@Valid FornecedorJuridicoDTO fornecedorJuridicoDTO) {
		List<String> erros = new ArrayList<>();
		if (fornecedorJuridicoDTO.getId() == null) {
			uniquenessValidation(fornecedorJuridicoDTO.getNome(), null, "nome",
					valor -> pessoaRepository.existsByNome(valor), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getIe(), null, "ie", valor -> juridicaRepository.existsByIe(valor),
					erros);
			uniquenessValidation(fornecedorJuridicoDTO.getCnpj(), null, "cnpj",
					valor -> juridicaRepository.existsByCnpj(valor), erros);
			return erros;
		} else {
			UUID id = fornecedorJuridicoDTO.getId();
			uniquenessValidation(fornecedorJuridicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getIe(), id, "rg",
					valor -> juridicaRepository.existsByIeAndIdNot(valor, id), erros);
			uniquenessValidation(fornecedorJuridicoDTO.getCnpj(), id, "cpf",
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
		return fornecedorRepository.existsById(id);
	}

	public FornecedorFisicoDTO findPessoaFisicaById(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"FÍSICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é Física.");
		}
		Fisica fisica = fisicaRepository.findById(id).get();
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(id);
		// Mapear para FornecedorFisicoDTO
		return fornecedorMapper.toFornecedorFisicoDTO(fisica, fornecedor);
	}

	public FornecedorJuridicoDTO findPessoaJuridicaById(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"JURÍDICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é  Jurídica.");
		}
		Juridica juridica = juridicaRepository.findById(id).get();
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(id);
		// Mapear para FornecedorJuridicoDTO
		return fornecedorMapper.toFornecedorJuridicoDTO(juridica, fornecedor);
	}
}
