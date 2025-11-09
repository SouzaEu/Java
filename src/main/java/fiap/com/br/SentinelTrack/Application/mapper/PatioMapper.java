package fiap.com.br.SentinelTrack.Application.mapper;

import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PatioMapper {
    
    private final MotoMapper motoMapper;
    
    public PatioMapper(MotoMapper motoMapper) {
        this.motoMapper = motoMapper;
    }
    
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
    
    public void updateEntity(Patio patio, CreatePatioDTO updateDTO) {
        if (patio == null || updateDTO == null) return;
        
        patio.setNome(updateDTO.getNome());
        patio.setEndereco(updateDTO.getEndereco());
        patio.setComplemento(updateDTO.getComplemento());
        patio.setAreaM2(updateDTO.getAreaM2());
        patio.setIdLocalidade(updateDTO.getIdLocalidade());
    }
}
