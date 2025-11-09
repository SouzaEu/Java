package fiap.com.br.SentinelTrack.Application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;

import fiap.com.br.SentinelTrack.Api.exception.PatioNotFoundException;
import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.mapper.FixedPatioMapper;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Service CORRIGIDO para gestão de pátios
 * Resolve todos os problemas de compilação e dependências
 */
@Service("fixedPatioService")
@Transactional
@Validated
@Slf4j
public class FixedPatioService {

    private final PatioRepository repository;
    private final FixedPatioMapper mapper;

    public FixedPatioService(PatioRepository repository, 
                           @Qualifier("fixedPatioMapper") FixedPatioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Lista todos os pátios
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
     * SOBRECARGA: Atualiza com CreatePatioDTO (compatibilidade)
     */
    public PatioDTO atualizar(@NotNull @Positive Long id, @Valid CreatePatioDTO updateDTO) {
        log.info("Atualizando pátio ID: {} (CreateDTO)", id);
        
        Patio patio = repository.findById(id)
                .orElseThrow(() -> new PatioNotFoundException(id));
        
        mapper.updateEntity(patio, updateDTO);
        Patio updatedPatio = repository.save(patio);
        
        log.info("Pátio atualizado com sucesso. ID: {}", id);
        return mapper.toDTO(updatedPatio);
    }

    /**
     * Remove pátio
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
     */
    @Transactional(readOnly = true)
    public boolean existe(@NotNull @Positive Long id) {
        return repository.existsById(id);
    }

    /**
     * Conta total de pátios
     */
    @Transactional(readOnly = true)
    public long contarTodos() {
        return repository.count();
    }

    /**
     * Método para uso interno - retorna entidade
     */
    @Transactional(readOnly = true)
    public Patio buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PatioNotFoundException(id));
    }
}
