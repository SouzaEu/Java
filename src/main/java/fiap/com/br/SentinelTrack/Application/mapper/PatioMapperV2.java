package fiap.com.br.SentinelTrack.Application.mapper;

import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper profissional para Patio
 * Implementa conversões entre entidades e DTOs de forma limpa
 */
@Component
public class PatioMapperV2 {
    
    private final MotoMapper motoMapper;
    
    public PatioMapperV2(MotoMapper motoMapper) {
        this.motoMapper = motoMapper;
    }
    
    /**
     * Converte entidade para DTO de resposta
     */
    public PatioDTO toDTO(Patio patio) {
        if (patio == null) return null;
        
        PatioDTO dto = new PatioDTO();
        dto.setId(patio.getId());
        dto.setNome(patio.getNome());
        dto.setEndereco(patio.getEndereco());
        dto.setComplemento(patio.getComplemento());
        dto.setAreaM2(patio.getAreaM2());
        dto.setIdLocalidade(patio.getIdLocalidade());
        
        if (patio.getMotos() != null) {
            dto.setMotos(patio.getMotos().stream()
                    .map(motoMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Converte DTO de criação para entidade
     */
    public Patio toEntity(CreatePatioDTO createDTO) {
        if (createDTO == null) return null;
        
        Patio patio = new Patio();
        patio.setNome(createDTO.getNome());
        patio.setEndereco(createDTO.getEndereco());
        patio.setComplemento(createDTO.getComplemento());
        patio.setAreaM2(createDTO.getAreaM2());
        patio.setIdLocalidade(createDTO.getIdLocalidade());
        
        return patio;
    }
    
    /**
     * Atualiza entidade com dados do DTO de atualização
     * IMPORTANTE: Não cria nova instância, atualiza a existente
     */
    public void updateEntity(Patio patio, UpdatePatioDTO updateDTO) {
        if (patio == null || updateDTO == null) return;
        
        patio.setNome(updateDTO.getNome());
        patio.setEndereco(updateDTO.getEndereco());
        patio.setComplemento(updateDTO.getComplemento());
        patio.setAreaM2(updateDTO.getAreaM2());
        patio.setIdLocalidade(updateDTO.getIdLocalidade());
    }
    
    /**
     * Converte DTO de atualização para DTO de criação
     * Útil para reutilizar formulários
     */
    public CreatePatioDTO toCreateDTO(UpdatePatioDTO updateDTO) {
        if (updateDTO == null) return null;
        
        CreatePatioDTO createDTO = new CreatePatioDTO();
        createDTO.setNome(updateDTO.getNome());
        createDTO.setEndereco(updateDTO.getEndereco());
        createDTO.setComplemento(updateDTO.getComplemento());
        createDTO.setAreaM2(updateDTO.getAreaM2());
        createDTO.setIdLocalidade(updateDTO.getIdLocalidade());
        
        return createDTO;
    }
    
    /**
     * Converte PatioDTO para UpdatePatioDTO
     * Útil para formulários de edição
     */
    public UpdatePatioDTO toUpdateDTO(PatioDTO patioDTO) {
        if (patioDTO == null) return null;
        
        UpdatePatioDTO updateDTO = new UpdatePatioDTO();
        updateDTO.setNome(patioDTO.getNome());
        updateDTO.setEndereco(patioDTO.getEndereco());
        updateDTO.setComplemento(patioDTO.getComplemento());
        updateDTO.setAreaM2(patioDTO.getAreaM2());
        updateDTO.setIdLocalidade(patioDTO.getIdLocalidade());
        
        return updateDTO;
    }
}
