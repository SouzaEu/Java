package fiap.com.br.SentinelTrack.Application.mapper;

import fiap.com.br.SentinelTrack.Application.dto.CreateMotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Domain.models.Moto;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import org.springframework.stereotype.Component;

@Component
public class MotoMapper {
    
    public MotoDTO toDTO(Moto moto) {
        if (moto == null) return null;
        
        MotoDTO dto = new MotoDTO();
        dto.setId(moto.getId());
        dto.setModelo(moto.getModelo());
        dto.setPlaca(moto.getPlaca());
        dto.setStatus(moto.getStatus());
        dto.setDataEntrada(moto.getDataEntrada());
        
        if (moto.getPatio() != null) {
            dto.setIdPatio(moto.getPatio().getId());
            dto.setNomePatio(moto.getPatio().getNome());
        }
        
        return dto;
    }
    
    public Moto toEntity(CreateMotoDTO createDTO, Patio patio) {
        if (createDTO == null) return null;
        
        Moto moto = new Moto();
        moto.setModelo(createDTO.getModelo());
        moto.setPlaca(createDTO.getPlaca());
        moto.setStatus(createDTO.getStatus());
        moto.setDataEntrada(createDTO.getDataEntrada());
        moto.setPatio(patio);
        
        return moto;
    }
    
    public void updateEntity(Moto moto, CreateMotoDTO updateDTO, Patio patio) {
        if (moto == null || updateDTO == null) return;
        
        moto.setModelo(updateDTO.getModelo());
        moto.setPlaca(updateDTO.getPlaca());
        moto.setStatus(updateDTO.getStatus());
        moto.setDataEntrada(updateDTO.getDataEntrada());
        if (patio != null) {
            moto.setPatio(patio);
        }
    }
}
