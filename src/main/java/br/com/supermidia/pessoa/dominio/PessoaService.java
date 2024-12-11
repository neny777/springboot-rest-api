package br.com.supermidia.pessoa.dominio;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Pessoa findById(UUID id) {
        return pessoaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa não encontrado"));
    }

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public void delete(UUID id) {
        pessoaRepository.deleteById(id);
    }
}
