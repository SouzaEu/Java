package fiap.com.br.SentinelTrack.Application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fiap.com.br.SentinelTrack.Application.dto.CreateMotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.mapper.MotoMapper;
import fiap.com.br.SentinelTrack.Domain.models.Moto;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.MotoRepository;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioService patioService;
    private final MotoMapper mapper;

    public MotoService(MotoRepository motoRepository, PatioService patioService, MotoMapper mapper) {
        this.motoRepository = motoRepository;
        this.patioService = patioService;
        this.mapper = mapper;
    }

    public List<MotoDTO> listarTodas() {
        return motoRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<MotoDTO> buscarPorId(Long id) {
        return motoRepository.findById(id)
                .map(mapper::toDTO);
    }

    public Optional<MotoDTO> buscarPorPlaca(String placa) {
        return motoRepository.findByPlaca(placa)
                .map(mapper::toDTO);
    }

    public List<MotoDTO> buscarPorPatio(Long idPatio) {
        return motoRepository.findByPatioId(idPatio)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MotoDTO> buscarPorStatus(String status) {
        return motoRepository.findByStatus(status)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public MotoDTO criar(CreateMotoDTO createDTO) {
        Optional<Patio> patioOpt = patioService.buscarEntidadePorId(createDTO.getIdPatio());
        if (patioOpt.isEmpty()) {
            throw new RuntimeException("Pátio não encontrado com ID: " + createDTO.getIdPatio());
        }

        // Verificar se placa já existe
        if (motoRepository.findByPlaca(createDTO.getPlaca()).isPresent()) {
            throw new RuntimeException("Já existe uma moto com a placa: " + createDTO.getPlaca());
        }

        Moto moto = mapper.toEntity(createDTO, patioOpt.get());
        Moto savedMoto = motoRepository.save(moto);
        return mapper.toDTO(savedMoto);
    }

    public Optional<MotoDTO> atualizar(Long id, CreateMotoDTO updateDTO) {
        return motoRepository.findById(id)
                .map(moto -> {
                    // Verificar se a nova placa já existe em outra moto
                    Optional<Moto> existingMoto = motoRepository.findByPlaca(updateDTO.getPlaca());
                    if (existingMoto.isPresent() && !existingMoto.get().getId().equals(id)) {
                        throw new RuntimeException("Já existe uma moto com a placa: " + updateDTO.getPlaca());
                    }

                    Optional<Patio> patioOpt = patioService.buscarEntidadePorId(updateDTO.getIdPatio());
                    if (patioOpt.isEmpty()) {
                        throw new RuntimeException("Pátio não encontrado com ID: " + updateDTO.getIdPatio());
                    }

                    mapper.updateEntity(moto, updateDTO, patioOpt.get());
                    Moto updatedMoto = motoRepository.save(moto);
                    return mapper.toDTO(updatedMoto);
                });
    }

    public boolean deletar(Long id) {
        if (motoRepository.existsById(id)) {
            motoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<MotoDTO> buscarPorModelo(String modelo) {
        return motoRepository.findByModeloContainingIgnoreCase(modelo)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
