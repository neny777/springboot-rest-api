package br.com.supermidia.pessoa.dominio;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class FisicaService {

    private final FisicaRepository pessoaFisicaRepository;

    public FisicaService(FisicaRepository pessoaFisicaRepository) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
    }

    public List<Fisica> findAll() {
        return pessoaFisicaRepository.findAll();
    }

    public Fisica findById(UUID id) {
        return pessoaFisicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa Física não encontrada"));
    }

    public Fisica save(Fisica pessoaFisica) {
        return pessoaFisicaRepository.save(pessoaFisica);
    }

    public void delete(UUID id) {
        pessoaFisicaRepository.deleteById(id);
    }
}

