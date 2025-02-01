package br.com.supermidia.pessoa.cliente;

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
public class ClienteService {
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private FisicaRepository fisicaRepository;
	@Autowired
	private JuridicaRepository juridicaRepository;
	@Autowired
	private ClienteMapper clienteMapper;

	@Transactional
	public Cliente saveFisico(ClienteFisicoDTO dto) {
		Cliente cliente = clienteMapper.toClienteFisico(dto);
		if (dto.getId() != null) {
			Fisica fisica = fisicaRepository.findById(dto.getId()).get();
			cliente.setPessoa(fisica);
			if (clienteRepository.existsById(dto.getId())) {
				cliente.setId(dto.getId());
			} else {
				cliente.setId(null);
			}
		}
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente saveJuridico(ClienteJuridicoDTO dto) {
		Cliente cliente = clienteMapper.toClienteJuridico(dto);
		if (dto.getId() != null) {
			Juridica juridica = juridicaRepository.findById(dto.getId()).get();
			cliente.setPessoa(juridica);
			if (clienteRepository.existsById(dto.getId())) {
				cliente.setId(dto.getId());
			} else {
				cliente.setId(null);
			}
		}
		return clienteRepository.save(cliente);
	}

	@Transactional
	public void editarClienteFisico(UUID id, ClienteFisicoDTO clienteFisicoDTO) {
		// Busca o cliente existente
		Cliente cliente = clienteRepository.findByPessoaId(clienteFisicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
		// Atualiza os dados do cliente usando o mapper
		clienteMapper.updateClienteFisicoFromDTO(clienteFisicoDTO, cliente);
		// Salva o cliente atualizado
		clienteRepository.save(cliente);
	}

	@Transactional
	public void editarClienteJuridico(UUID id, ClienteJuridicoDTO clienteJuridicoDTO) {
		// Busca o cliente existente
		Cliente cliente = clienteRepository.findByPessoaId(clienteJuridicoDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
		// Atualiza os dados do cliente usando o mapper
		clienteMapper.updateClienteJuridicoFromDTO(clienteJuridicoDTO, cliente);
		// Salva o cliente atualizado
		clienteRepository.save(cliente);
	}

	@Transactional
	public void delete(UUID clienteId) {

		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
		Fisica fisica;
		if ((cliente.getPessoa()).getClass() == Fisica.class) {
			fisica = fisicaRepository.findById(clienteId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada."));
			if (pessoaService.clienteTemOutroPapel(fisica.getId())) {
				clienteRepository.deleteById(clienteId);
				return;
			} else {
				clienteRepository.deleteById(clienteId);
				fisicaRepository.deleteById(clienteId);
				pessoaRepository.deleteById(clienteId);
				return;
			}
		}
		Juridica juridica;
		if ((cliente.getPessoa()).getClass() == Juridica.class) {
			juridica = juridicaRepository.findById(clienteId)
					.orElseThrow(() -> new IllegalArgumentException("Pessoa jurídica não encontrada."));
			if (pessoaService.clienteTemOutroPapel(juridica.getId())) {
				clienteRepository.deleteById(clienteId);

				return;
			} else {
				clienteRepository.deleteById(clienteId);
				juridicaRepository.deleteById(clienteId);
				pessoaRepository.deleteById(clienteId);
				return;
			}
		}
	}

	public List<ClienteDTO> listarClientesDTO() {
		List<Cliente> clientes = clienteRepository.findAll();
		List<ClienteDTO> clientesDTO = new ArrayList<>();
		for (Cliente cliente : clientes) {
			ClienteDTO dto = new ClienteDTO();
			dto.setId(cliente.getId());
			dto.setNome(cliente.getPessoa().getNome());
			dto.setEmail(cliente.getPessoa().getEmail());
			dto.setTelefone(cliente.getPessoa().getTelefone());
			dto.setMunicipio(cliente.getPessoa().getMunicipio());
			dto.setUf(cliente.getPessoa().getUf());
			dto.setTipo(cliente.getPessoa().getTipo());
			clientesDTO.add(dto);
		}
		return clientesDTO;
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

	public ClienteFisicoDTO buscarClienteFisico(UUID id) {
		// Buscar o cliente pelo ID
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(cliente.getPessoa() instanceof Fisica fisica)) {
			throw new IllegalStateException("A pessoa associada ao cliente não é do tipo Fisica.");
		}
		// Mapear o cliente e a entidade Fisica para ClienteFisicoDTO
		return clienteMapper.toClienteFisicoDTO(fisica, cliente);
	}

	public ClienteJuridicoDTO buscarClienteJuridico(UUID id) {
		// Buscar o cliente pelo ID
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));
		// Garantir que a pessoa associada é do tipo Fisica
		if (!(cliente.getPessoa() instanceof Juridica juridica)) {
			throw new IllegalStateException("A pessoa associada ao cliente não é do tipo Jurídica.");
		}
		// Mapear o cliente e a entidade Fisica para ClienteFisicoDTO
		return clienteMapper.toClienteJuridicoDTO(juridica, cliente);
	}

	public List<String> validarAtributosFisicoUnicos(ClienteFisicoDTO clienteFisicoDTO) {
		List<String> erros = new ArrayList<>();
		if (clienteFisicoDTO.getId() == null) {
			validarUnicidade(clienteFisicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			validarUnicidade(clienteFisicoDTO.getEmail(), null, "email", valor -> pessoaRepository.existsByEmail(valor),
					erros);
			validarUnicidade(clienteFisicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			validarUnicidade(clienteFisicoDTO.getRg(), null, "rg", valor -> fisicaRepository.existsByRg(valor), erros);
			validarUnicidade(clienteFisicoDTO.getCpf(), null, "cpf", valor -> fisicaRepository.existsByCpf(valor),
					erros);
			return erros;
		} else {
			UUID id = clienteFisicoDTO.getId();
			validarUnicidade(clienteFisicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			validarUnicidade(clienteFisicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			validarUnicidade(clienteFisicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			validarUnicidade(clienteFisicoDTO.getRg(), id, "rg",
					valor -> fisicaRepository.existsByRgAndIdNot(valor, id), erros);
			validarUnicidade(clienteFisicoDTO.getCpf(), id, "cpf",
					valor -> fisicaRepository.existsByCpfAndIdNot(valor, id), erros);
			return erros;
		}
	}

	public List<String> validarAtributosJuridicoUnicos(@Valid ClienteJuridicoDTO clienteJuridicoDTO) {
		List<String> erros = new ArrayList<>();
		if (clienteJuridicoDTO.getId() == null) {
			validarUnicidade(clienteJuridicoDTO.getNome(), null, "nome", valor -> pessoaRepository.existsByNome(valor),
					erros);
			validarUnicidade(clienteJuridicoDTO.getEmail(), null, "email",
					valor -> pessoaRepository.existsByEmail(valor), erros);
			validarUnicidade(clienteJuridicoDTO.getTelefone(), null, "telefone",
					valor -> pessoaRepository.existsByTelefone(valor), erros);
			validarUnicidade(clienteJuridicoDTO.getIe(), null, "ie", valor -> juridicaRepository.existsByIe(valor),
					erros);
			validarUnicidade(clienteJuridicoDTO.getCnpj(), null, "cnpj",
					valor -> juridicaRepository.existsByCnpj(valor), erros);
			return erros;
		} else {
			UUID id = clienteJuridicoDTO.getId();
			validarUnicidade(clienteJuridicoDTO.getNome(), id, "nome",
					valor -> pessoaRepository.existsByNomeAndIdNot(valor, id), erros);
			validarUnicidade(clienteJuridicoDTO.getEmail(), id, "email",
					valor -> pessoaRepository.existsByEmailAndIdNot(valor, id), erros);
			validarUnicidade(clienteJuridicoDTO.getTelefone(), id, "telefone",
					valor -> pessoaRepository.existsByTelefoneAndIdNot(valor, id), erros);
			validarUnicidade(clienteJuridicoDTO.getIe(), id, "rg",
					valor -> juridicaRepository.existsByIeAndIdNot(valor, id), erros);
			validarUnicidade(clienteJuridicoDTO.getCnpj(), id, "cpf",
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

	public boolean existeClientePorId(UUID id) {
		return clienteRepository.existsById(id);
	}

	public ClienteFisicoDTO buscarPessoaFisica(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"FÍSICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é Física.");
		}
		Fisica fisica = fisicaRepository.findById(id).get();
		Cliente cliente = new Cliente();
		cliente.setId(id);
		// Mapear para ClienteFisicoDTO
		return clienteMapper.toClienteFisicoDTO(fisica, cliente);
	}

	public ClienteJuridicoDTO buscarPessoaJuridica(UUID id) {
		// Buscar a pessoa independente do tipo
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
		// Verificar se a pessoa é do tipo correto
		if (!"JURÍDICA".equals(pessoa.getTipo())) {
			throw new IllegalStateException("Pessoa selecionada não é  Jurídica.");
		}
		Juridica juridica = juridicaRepository.findById(id).get();
		Cliente cliente = new Cliente();
		cliente.setId(id);
		// Mapear para ClienteJuridicoDTO
		return clienteMapper.toClienteJuridicoDTO(juridica, cliente);
	}
}
