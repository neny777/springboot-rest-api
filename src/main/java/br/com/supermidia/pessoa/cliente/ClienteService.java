package br.com.supermidia.pessoa.cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.supermidia.pessoa.dominio.Fisica;
import br.com.supermidia.pessoa.dominio.FisicaRepository;
import br.com.supermidia.pessoa.dominio.Juridica;
import br.com.supermidia.pessoa.dominio.JuridicaRepository;
import br.com.supermidia.pessoa.dominio.Pessoa;
import br.com.supermidia.pessoa.dominio.PessoaAssociationService;
import br.com.supermidia.pessoa.dominio.PessoaRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private FisicaRepository fisicaRepository;

	@Autowired
	private JuridicaRepository juridicaRepository;

	@Autowired
	private PessoaAssociationService pessoaAssociationService;

	@Autowired
	private ClienteMapper clienteMapper;

	@Transactional
	public Cliente saveFisico(ClienteFisicoDTO clienteDto) {
		Fisica fisica = clienteMapper.toFisica(clienteDto);

		if (fisica.getId() != null) {
			// Garantir que a entidade está sincronizada com o Hibernate
			fisica = fisicaRepository.findById(fisica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Pessoa Física não encontrada para atualização."));
		}

		Pessoa pessoa = pessoaRepository.save(fisica);

		if (clienteRepository.existsByPessoaId(pessoa.getId())) {
			throw new IllegalArgumentException("Essa pessoa já está associada como cliente.");
		}

		Cliente cliente = new Cliente();
		cliente.setPessoa(pessoa);
		cliente.setTipo(clienteDto.getTipo());
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente saveJuridico(ClienteJuridicoDTO clienteDto) {
		System.out.println("Incio método saveJuridico");
		Pessoa pessoa;

		if (clienteDto.getId() != null) {
			// Verifica se a pessoa já existe no banco de dados
			Optional<Pessoa> existingPessoa = pessoaRepository.findById(clienteDto.getId());
			if (existingPessoa.isPresent()) {
				pessoa = existingPessoa.get();
				if (pessoa instanceof Juridica) {
					clienteMapper.updateJuridicaFromDTO(clienteDto, (Juridica) pessoa);
				} else {
					throw new IllegalArgumentException("A pessoa encontrada não corresponde a uma Pessoa Jurídica.");
				}
			} else {
				throw new IllegalArgumentException("Pessoa com o ID fornecido não encontrada.");
			}
		} else {
			// Cria e salva uma nova Pessoa Jurídica
			pessoa = clienteMapper.toJuridica(clienteDto);
			pessoa = pessoaRepository.save(pessoa); // Salva a Pessoa antes de associá-la ao Cliente
		}

		// Verifica se a pessoa já é um cliente
		if (clienteRepository.existsByPessoaId(pessoa.getId())) {
			throw new IllegalArgumentException("Essa pessoa já está associada como cliente.");
		}

		// Cria e salva o Cliente associado à Pessoa
		Cliente cliente = new Cliente();
		cliente.setPessoa(pessoa);
		return clienteRepository.save(cliente);
	}

	/*
	 * private Pessoa saveOrUpdatePessoa(Pessoa pessoa) { Optional<Pessoa>
	 * existingPessoa = pessoaRepository.findById(pessoa.getId()); if
	 * (existingPessoa.isPresent()) { Pessoa existing = existingPessoa.get(); if
	 * (pessoa instanceof Fisica && existing instanceof Fisica) {
	 * clienteMapper.updateFisicaFromDTO((ClienteFisicoDTO)
	 * clienteMapper.toClienteFisicoDTO((Fisica) pessoa), (Fisica) existing); } else
	 * if (pessoa instanceof Juridica && existing instanceof Juridica) {
	 * clienteMapper.updateJuridicaFromDTO( (ClienteJuridicoDTO)
	 * clienteMapper.toClienteJuridicoDTO((Juridica) pessoa), (Juridica) existing);
	 * } return existing; } else { return pessoaRepository.save(pessoa); } }
	 */
	@Transactional
	public Cliente updateFisico(UUID id, ClienteFisicoDTO clienteDto) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

		Pessoa pessoa = cliente.getPessoa();
		if (!(pessoa instanceof Fisica)) {
			throw new IllegalArgumentException("A pessoa associada não corresponde a uma Pessoa Física.");
		}

		clienteMapper.updateFisicaFromDTO(clienteDto, (Fisica) pessoa);
		pessoaRepository.save(pessoa);

		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente updateJuridico(UUID id, ClienteJuridicoDTO clienteDto) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

		Pessoa pessoa = cliente.getPessoa();
		if (!(pessoa instanceof Juridica)) {
			throw new IllegalArgumentException("A pessoa associada não corresponde a uma Pessoa Jurídica.");
		}

		clienteMapper.updateJuridicaFromDTO(clienteDto, (Juridica) pessoa);
		pessoaRepository.save(pessoa);

		return clienteRepository.save(cliente);
	}

	@Transactional
	public void delete(UUID clienteId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

		Pessoa pessoa = cliente.getPessoa();
		clienteRepository.delete(cliente);

		// Verifica se a Pessoa está associada a outras composições
		boolean hasOtherCompositions = hasOtherAssociations(pessoa);
		if (!hasOtherCompositions) {
			pessoaRepository.delete(pessoa);
		}
	}

	private boolean hasOtherAssociations(Pessoa pessoa) {
		return pessoaAssociationService.hasAnyAssociation(pessoa.getId());
	}

	public List<ClienteDTO> listarTodosClientes() {
		System.out.println("Método listar clientes Service");
		List<Object[]> resultados = clienteRepository.findAllClientes();
		return resultados.stream().map(this::mapToClienteDTO).collect(Collectors.toList());
	}

	private ClienteDTO mapToClienteDTO(Object[] row) {
		ClienteDTO clienteDTO = new ClienteDTO();

		// System.out.println(Arrays.toString(row));

		clienteDTO.setId(UUID.fromString((String) row[0])); // clienteId
		clienteDTO.setNome(row[1] != null ? (String) row[1] : "Nome não informado"); // nome
		clienteDTO.setEmail(row[2] != null ? (String) row[2] : "Email não informado"); // email
		clienteDTO.setTelefone(row[3] != null ? (String) row[3] : "Telefone não informado"); // telefone
		clienteDTO.setMunicipio(row[4] != null ? (String) row[4] : "Município não informado"); // municipio
		clienteDTO.setUf(row[5] != null ? (String) row[5] : "UF não informada"); // uf
		// Tipo (enum)
		if (row[6] != null) {
			clienteDTO.setTipo(Cliente.Tipo.valueOf(row[6].toString())); // Converte Character para String
		} else {
			throw new IllegalArgumentException("Tipo do cliente não pode ser nulo.");
		}

		return clienteDTO;
	}

	@Transactional(readOnly = true)
	public ClienteDTO buscarPorId(UUID id) {
		// Buscar na tabela de pessoas físicas
		Optional<Fisica> fisicaOptional = fisicaRepository.findById(id);
		if (fisicaOptional.isPresent()) {
			Fisica fisica = fisicaOptional.get();
			Cliente cliente = clienteRepository.findByPessoaId(fisica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Cliente associado não encontrado."));
			return clienteMapper.toClienteFisicoDTO(fisica, cliente);
		}

		// Buscar na tabela de pessoas jurídicas
		Optional<Juridica> juridicaOptional = juridicaRepository.findById(id);
		if (juridicaOptional.isPresent()) {
			Juridica juridica = juridicaOptional.get();
			Cliente cliente = clienteRepository.findByPessoaId(juridica.getId())
					.orElseThrow(() -> new IllegalArgumentException("Cliente associado não encontrado."));
			return clienteMapper.toClienteJuridicoDTO(juridica, cliente);
		}

		// Se não encontrado em nenhum dos dois
		throw new IllegalArgumentException("Cliente não encontrado.");
	}
}
