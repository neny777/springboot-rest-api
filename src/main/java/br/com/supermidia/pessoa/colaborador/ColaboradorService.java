package br.com.supermidia.pessoa.colaborador;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private ColaboradorMapper colaboradorMapper;

    // Método para encontrar todos os colaboradores
    public List<ColaboradorDTO> findAll() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return colaboradores.stream()
                .map(colaboradorMapper::toDto) // Usa o MapStruct para converter para DTO
                .collect(Collectors.toList());
    }

    // Método para encontrar um colaborador por ID e retornar um DTO
    public ColaboradorDTO findById(UUID id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        return colaboradorMapper.toDto(colaborador);
    }

    // Buscar a entity Colaborador por ID (para fins de atualização)
    public Colaborador findEntityById(UUID id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }

    // Atualizar o colaborador existente com os dados do ColaboradorDTO
    public Colaborador updateColaborador(Colaborador colaboradorExistente, ColaboradorDTO colaboradorDTO) {
        colaboradorMapper.updateEntityFromDto(colaboradorDTO, colaboradorExistente);
        return colaboradorRepository.save(colaboradorExistente);
    }

    // Método para salvar um ColaboradorDTO (conversão do DTO para entity)
    public Colaborador saveColaborador(ColaboradorDTO colaboradorDTO) {
        Colaborador colaborador = colaboradorMapper.toEntity(colaboradorDTO);
        return colaboradorRepository.save(colaborador);
    }

    // Método para deletar um colaborador por ID
    public void delete(UUID id) {
        colaboradorRepository.deleteById(id);
    }
}
