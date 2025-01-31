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
	public void editarFornecedorFisico(UUID id, FornecedorFisicoDTO fornecedorFisicoDTO) {
		// Busca o fornecedor existente
		Fornecedor fornecedor = fornecedorRepository.findByPessoaId(fornecedorFisicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));

		// Atualiza os dados do fornecedor usando o mapper
		fornecedorMapper.updateFornecedorFisicoFromDTO(fornecedorFisicoDTO, fornecedor);

		// Salva o fornecedor atualizado
		fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public void editarFornecedorJuridico(UUID id, FornecedorJuridicoDTO fornecedorJuridicoDTO) {
		System.out.println("Service edição fornecedor jurídico");
		System.out.println("ID do fornecedor jurídico: " + fornecedorJuridicoDTO.getId());
		// Busca o fornecedor existente
		Fornecedor fornecedor = fornecedorRepository.findByPessoaId(fornecedorJuridicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));

		// Atualiza os dados do fornecedor usando o mapper
		fornecedorMapper.updateFornecedorJuridicoFromDTO(fornecedorJuridicoDTO, fornecedor);

		System.out.println("ID do fornecedor depois de buscado e atualizado: " + fornecedorJuridicoDTO.getId());

		// Salva o fornecedor atualizado
		fornecedorRepository.save(fornecedor);
	}

	@Transactional
	public void delete(UUID fornecedorId) {
		System.out.println("Service deletar por id");
		Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
				.orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));

		System.out.println("Fornecedor localizado");
		Fisica fisica;
		if ((fornecedor.getPessoa()).getClass() == Fisica.class) {
			System.out.println("Fornecedor físico");
			fisica = fisicaRepository.findById(fornecedorId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada."));

			if (pessoaService.fornecedorTemOutroPapel(fisica.getId())) {
				System.out.println("Fornecedor jurídico tem outro papel");
				fornecedorRepository.deleteById(fornecedorId);
				System.out.println("Desassociando de fornecedor");
				return;

			} else {
				System.out.println("Fornecedor jurídico não tem outro papel");
				fornecedorRepository.deleteById(fornecedorId);
				System.out.println("Deletando fornecedor");
				fisicaRepository.deleteById(fornecedorId);
				System.out.println("Deletando pessoa física");
				pessoaRepository.deleteById(fornecedorId);
				System.out.println("Deletando pessoa");
				return;
			}
		}

		Juridica juridica;
		if ((fornecedor.getPessoa()).getClass() == Juridica.class) {
			System.out.println("Fornecedor jurídico");

			juridica = juridicaRepository.findById(fornecedorId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa jurídica não encontrada."));
			System.out.println(juridica.getNome());

			if (pessoaService.fornecedorTemOutroPapel(juridica.getId())) {
				System.out.println("Fornecedor jurídico tem outro papel");
				fornecedorRepository.deleteById(fornecedorId);
				System.out.println("Desassociando de fornecedor");
				return;

			} else {
				System.out.println("Fornecedor jurídico não tem outro papel");
				fornecedorRepository.deleteById(fornecedorId);
				System.out.println("Deletando fornecedor");
				juridicaRepository.deleteById(fornecedorId);
				System.out.println("Deletando pessoa jurídica");
				pessoaRepository.deleteById(fornecedorId);
				System.out.println("Deletando pessoa");
				return;
			}
		}
	}

	public List<FornecedorDTO> listarFornecedoresDTO() {

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
	public FornecedorDTO buscarPorId(UUID id) {
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

	public FornecedorFisicoDTO buscarFornecedorFisico(UUID id) {
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

	public FornecedorJuridicoDTO buscarFornecedorJuridico(UUID id) {
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

	public List<String> validarAtributosFisicoUnicos(FornecedorFisicoDTO fornecedorFisicoDTO) {
		List<String> erros = new ArrayList<>();

		System.out.println("Service validar atributos únicos físico");
		System.out.println("id do dto: " + fornecedorFisicoDTO.getId());

		if (fornecedorFisicoDTO.getId() == null) {
			validarUnicidade(fornecedorFisicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			validarUnicidade(fornecedorFisicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			validarUnicidade(fornecedorFisicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			validarUnicidade(fornecedorFisicoDTO.getRg(), null, "rg", valor -> fisicaRepository.existsByRg(valor),
					erros);
			validarUnicidade(fornecedorFisicoDTO.getCpf(), null, "cpf", valor -> fisicaRepository.existsByCpf(valor),
					erros);

			return erros;
		} else {
			UUID id = fornecedorFisicoDTO.getId();

			validarUnicidade(fornecedorFisicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorFisicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorFisicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorFisicoDTO.getRg(), id, "rg",
					valor -> fisicaRepository.existsByRgAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorFisicoDTO.getCpf(), id, "cpf",
					valor -> fisicaRepository.existsByCpfAndIdNot(valor, id), erros);

			return erros;
		}
	}

	public List<String> validarAtributosJuridicoUnicos(@Valid FornecedorJuridicoDTO fornecedorJuridicoDTO) {
		List<String> erros = new ArrayList<>();

		if (fornecedorJuridicoDTO.getId() == null) {
			validarUnicidade(fornecedorJuridicoDTO.getNome(), null, "nome",
					valor -> pessoaRepository.existsByNome(valor), erros);
			validarUnicidade(fornecedorJuridicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			validarUnicidade(fornecedorJuridicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			validarUnicidade(fornecedorJuridicoDTO.getIe(), null, "ie", valor -> juridicaRepository.existsByIe(valor),
					erros);
			validarUnicidade(fornecedorJuridicoDTO.getCnpj(), null, "cnpj",
					valor -> juridicaRepository.existsByCnpj(valor), erros);

			return erros;
		} else {
			UUID id = fornecedorJuridicoDTO.getId();

			validarUnicidade(fornecedorJuridicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorJuridicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorJuridicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorJuridicoDTO.getIe(), id, "rg",
					valor -> juridicaRepository.existsByIeAndIdNot(valor, id), erros);
			validarUnicidade(fornecedorJuridicoDTO.getCnpj(), id, "cpf",
					valor -> juridicaRepository.existsByCnpjAndIdNot(valor, id), erros);

			return erros;
		}
	}

	private void validarUnicidade(String valor, UUID id, String campo, Function<String, Boolean> verificaUnicidade,
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

	public boolean existeFornecedorPorId(UUID id) {
		return fornecedorRepository.existsById(id);
	}

	public FornecedorFisicoDTO buscarPessoaFisica(UUID id) {
		System.out.println("Service: buscarPessoaFisica");
		System.out.println("Id: " + id);
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

		System.out.println("Id física: " + fisica.getId());

		// Mapear para FornecedorFisicoDTO
		return fornecedorMapper.toFornecedorFisicoDTO(fisica, fornecedor);
	}

	public FornecedorJuridicoDTO buscarPessoaJuridica(UUID id) {
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
