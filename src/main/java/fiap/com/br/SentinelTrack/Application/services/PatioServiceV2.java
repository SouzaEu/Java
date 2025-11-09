package fiap.com.br.SentinelTrack.Application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;

import fiap.com.br.SentinelTrack.Api.exception.PatioNotFoundException;
import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.mapper.PatioMapper;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Service profissional para gestão de pátios
 * Implementa Single Responsibility Principle e tratamento adequado de exceções
 */
@Service
@Transactional
@Validated
@Slf4j
public class PatioServiceV2 {

    private final PatioRepository repository;
    private final PatioMapper mapper;

    public PatioServiceV2(PatioRepository repository, PatioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Lista todos os pátios
     * @return Lista de PatioDTO
     */
    @Transactional(readOnly = true)
    public List<PatioDTO> listarTodos() {
        log.debug("Listando todos os pátios");
        
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca pátio por ID
     * @param id ID do pátio
     * @return PatioDTO
     * @throws PatioNotFoundException se não encontrado
     */
    @Transactional(readOnly = true)
    public PatioDTO buscarPorId(@NotNull @Positive Long id) {
        log.debug("Buscando pátio por ID: {}", id);
        
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new PatioNotFoundException(id));
    }

    /**
     * Cria novo pátio
     * @param createDTO Dados para criação
     * @return PatioDTO criado
     */
    public PatioDTO criar(@Valid CreatePatioDTO createDTO) {
        log.info("Criando novo pátio: {}", createDTO.getNome());
        
        Patio patio = mapper.toEntity(createDTO);
        Patio savedPatio = repository.save(patio);
        
        log.info("Pátio criado com sucesso. ID: {}", savedPatio.getId());
        return mapper.toDTO(savedPatio);
    }

    /**
     * Atualiza pátio existente
     * @param id ID do pátio
     * @param updateDTO Dados para atualização
     * @return PatioDTO atualizado
     * @throws PatioNotFoundException se não encontrado
     */
    public PatioDTO atualizar(@NotNull @Positive Long id, @Valid UpdatePatioDTO updateDTO) {
        log.info("Atualizando pátio ID: {}", id);
        
        Patio patio = repository.findById(id)
                .orElseThrow(() -> new PatioNotFoundException(id));
        
        mapper.updateEntity(patio, updateDTO);
        Patio updatedPatio = repository.save(patio);
        
        log.info("Pátio atualizado com sucesso. ID: {}", id);
        return mapper.toDTO(updatedPatio);
    }

    /**
     * Remove pátio
     * @param id ID do pátio
     * @throws PatioNotFoundException se não encontrado
     */
    public void deletar(@NotNull @Positive Long id) {
        log.info("Removendo pátio ID: {}", id);
        
        if (!repository.existsById(id)) {
            throw new PatioNotFoundException(id);
        }
        
        repository.deleteById(id);
        log.info("Pátio removido com sucesso. ID: {}", id);
    }

    /**
     * Busca pátios por nome
     * @param nome Nome para busca (case insensitive)
     * @return Lista de PatioDTO
     */
    @Transactional(readOnly = true)
    public List<PatioDTO> buscarPorNome(String nome) {
        log.debug("Buscando pátios por nome: {}", nome);
        
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodos();
        }
        
        return repository.findByNomeContainingIgnoreCase(nome.trim())
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se pátio existe
     * @param id ID do pátio
     * @return true se existe
     */
    @Transactional(readOnly = true)
    public boolean existe(@NotNull @Positive Long id) {
        return repository.existsById(id);
    }

    /**
     * Conta total de pátios
     * @return Número total de pátios
     */
    @Transactional(readOnly = true)
    public long contarTodos() {
        return repository.count();
    }

    // Métodos para uso interno (retornam entidades) - DEPRECATED
    @Deprecated(since = "2.0", forRemoval = true)
    @Transactional(readOnly = true)
    public Patio buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PatioNotFoundException(id));
    }
}
