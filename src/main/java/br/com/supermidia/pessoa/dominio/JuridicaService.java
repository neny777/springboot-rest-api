package br.com.supermidia.pessoa.dominio;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class JuridicaService {

	private final JuridicaRepository pessoaJuridicaRepository;

	public JuridicaService(JuridicaRepository pessoaJuridicaRepository) {
		this.pessoaJuridicaRepository = pessoaJuridicaRepository;
	}

	public List<Juridica> findAll() {
		return pessoaJuridicaRepository.findAll();
	}

	public Juridica findById(UUID id) {
		return pessoaJuridicaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pessoa Jurídica não encontrada"));
	}

	public Juridica save(Juridica pessoaJuridica) {
		return pessoaJuridicaRepository.save(pessoaJuridica);
	}

	public void delete(UUID id) {
		pessoaJuridicaRepository.deleteById(id);
	}
}
