package fiap.com.br.SentinelTrack.Application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import fiap.com.br.SentinelTrack.Api.exception.PatioNotFoundException;
import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.mapper.PatioMapper;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;

@Service
public class PatioService {

    private final PatioRepository repository;
    private final PatioMapper mapper;

    public PatioService(PatioRepository repository, PatioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PatioDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PatioDTO> buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    public PatioDTO criar(CreatePatioDTO createDTO) {
        Patio patio = mapper.toEntity(createDTO);
        Patio savedPatio = repository.save(patio);
        return mapper.toDTO(savedPatio);
    }

    public Optional<PatioDTO> atualizar(Long id, CreatePatioDTO updateDTO) {
        return repository.findById(id)
                .map(patio -> {
                    mapper.updateEntity(patio, updateDTO);
                    Patio updatedPatio = repository.save(patio);
                    return mapper.toDTO(updatedPatio);
                });
    }

    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PatioDTO> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Métodos para uso interno (retornam entidades)
    public List<Patio> listar() {
        return repository.findAll();
    }

    public Optional<Patio> buscarEntidadePorId(Long id) {
        return repository.findById(id);
    }

    public Patio salvar(Patio patio) {
        return repository.save(patio);
    }
}
