package br.com.supermidia.pessoa.dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.cliente.ClienteRepository;
import br.com.supermidia.pessoa.colaborador.ColaboradorRepository;
import br.com.supermidia.pessoa.fornecedor.FornecedorRepository;
import br.com.supermidia.pessoa.parceiro.ParceiroRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private ParceiroRepository parceiroRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

	public List<Map<String, Object>> findPessoasByNome(String nome) {
		List<Pessoa> pessoas = pessoaRepository.findByNomeContainingIgnoreCase(nome);

		return pessoas.stream().map(pessoa -> {
			Map<String, Object> pessoaMap = new HashMap<>();
			pessoaMap.put("id", pessoa.getId());
			pessoaMap.put("nome", pessoa.getNome());
			return pessoaMap;
		}).collect(Collectors.toList());
	}
	
    public boolean clienteTemOutroPapel(UUID id) {
        return isColaborador(id) || isFornecedor(id) || isParceiro(id);
    }
    
	public boolean fornecedorTemOutroPapel(UUID id) {		
		return isCliente(id) || isColaborador(id) || isParceiro(id);
	}
	
	public boolean parceiroTemOutroPapel(UUID id) {		
		return isCliente(id) || isColaborador(id) || isFornecedor(id);
	}
    
    public boolean colaboradorTemOutroPapel(UUID id) {
        return isCliente(id) || isFornecedor(id) || isParceiro(id);
    }

    public boolean isCliente(UUID id) {
        return clienteRepository.existsByPessoaId(id);
    }
    
    public boolean isFornecedor(UUID id) {
        return fornecedorRepository.existsByPessoaId(id);
    }
    
    public boolean isParceiro(UUID id) {
        return parceiroRepository.existsByPessoaId(id);
    }

    public boolean isColaborador(UUID id) {
        return colaboradorRepository.existsByFisicaId(id);
    }
}
