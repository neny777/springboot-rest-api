package br.com.supermidia.pessoa.dominio;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.supermidia.pessoa.cliente.ClienteRepository;
import br.com.supermidia.pessoa.colaborador.ColaboradorRepository;

@Service
public class PessoaAssociationService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;


    public boolean hasAnyAssociation(UUID pessoaId) {
        return isCliente(pessoaId) || isColaborador(pessoaId);
    }

    public boolean isCliente(UUID pessoaId) {
        return clienteRepository.existsByPessoaId(pessoaId);
    }

    public boolean isColaborador(UUID pessoaId) {
        return colaboradorRepository.existsByFisicaId(pessoaId);
    }

	// Adicione métodos para outras associações conforme necessário.
}
