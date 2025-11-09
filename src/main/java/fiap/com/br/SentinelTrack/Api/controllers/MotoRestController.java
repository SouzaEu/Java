package fiap.com.br.SentinelTrack.Api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fiap.com.br.SentinelTrack.Application.dto.CreateMotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.services.MotoService;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller REST para integração com Mobile App
 * Challenge 2025 - 4º Sprint
 * 
 * Endpoints para CRUD de motos consumidos pelo React Native
 */
@RestController
@RequestMapping("/api/mobile/motos")
@CrossOrigin(origins = "*")
@Slf4j
public class MotoRestController {

    private final MotoService motoService;

    public MotoRestController(MotoService motoService) {
        this.motoService = motoService;
    }

    /**
     * Lista todas as motos ou filtra por status
     * GET /api/mobile/motos?status=DISPONIVEL
     */
    @GetMapping
    public ResponseEntity<?> listarMotos(@RequestParam(required = false) String status) {
        try {
            List<MotoDTO> motos;
            
            if (status != null && !status.isEmpty()) {
                motos = motoService.buscarPorStatus(status.toUpperCase());
                log.info("Listando motos com status: {}", status);
            } else {
                motos = motoService.listarTodas();
                log.info("Listando todas as motos");
            }

            return ResponseEntity.ok(Map.of(
                "success", true,
                "motos", motos,
                "total", motos.size(),
                "timestamp", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            log.error("Erro ao listar motos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Busca moto por placa
     * GET /api/mobile/motos/buscar/{placa}
     */
    @GetMapping("/buscar/{placa}")
    public ResponseEntity<?> buscarPorPlaca(@PathVariable String placa) {
        try {
            Optional<MotoDTO> moto = motoService.buscarPorPlaca(placa.toUpperCase());
            
            if (moto.isPresent()) {
                log.info("Moto encontrada: {}", placa);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "moto", moto.get(),
                    "found", true
                ));
            } else {
                log.warn("Moto não encontrada: {}", placa);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "success", false, 
                        "message", "Moto não encontrada",
                        "found", false
                    ));
            }
        } catch (Exception e) {
            log.error("Erro ao buscar moto por placa {}: {}", placa, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Cadastra nova moto
     * POST /api/mobile/motos
     */
    @PostMapping
    public ResponseEntity<?> criarMoto(@RequestBody CreateMotoDTO createDTO) {
        try {
            MotoDTO novaMoto = motoService.criar(createDTO);
            log.info("Nova moto cadastrada: {}", createDTO.getPlaca());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "moto", novaMoto,
                    "message", "Moto cadastrada com sucesso"
                ));
        } catch (RuntimeException e) {
            log.error("Erro ao cadastrar moto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro interno ao cadastrar moto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", "Erro interno do servidor"));
        }
    }

    /**
     * Atualiza moto existente
     * PUT /api/mobile/motos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMoto(@PathVariable Long id, @RequestBody CreateMotoDTO updateDTO) {
        try {
            Optional<MotoDTO> motoAtualizada = motoService.atualizar(id, updateDTO);
            
            if (motoAtualizada.isPresent()) {
                log.info("Moto atualizada: ID {}", id);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "moto", motoAtualizada.get(),
                    "message", "Moto atualizada com sucesso"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Moto não encontrada"));
            }
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar moto ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro interno ao atualizar moto ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", "Erro interno do servidor"));
        }
    }

    /**
     * Remove moto
     * DELETE /api/mobile/motos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMoto(@PathVariable Long id) {
        try {
            boolean deletada = motoService.deletar(id);
            
            if (deletada) {
                log.info("Moto deletada: ID {}", id);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Moto removida com sucesso"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Moto não encontrada"));
            }
        } catch (Exception e) {
            log.error("Erro ao deletar moto ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Busca motos por pátio
     * GET /api/mobile/motos/patio/{idPatio}
     */
    @GetMapping("/patio/{idPatio}")
    public ResponseEntity<?> buscarPorPatio(@PathVariable Long idPatio) {
        try {
            List<MotoDTO> motos = motoService.buscarPorPatio(idPatio);
            log.info("Buscando motos do pátio: {}", idPatio);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "motos", motos,
                "total", motos.size(),
                "idPatio", idPatio
            ));
        } catch (Exception e) {
            log.error("Erro ao buscar motos do pátio {}: {}", idPatio, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Busca motos por modelo
     * GET /api/mobile/motos/modelo/{modelo}
     */
    @GetMapping("/modelo/{modelo}")
    public ResponseEntity<?> buscarPorModelo(@PathVariable String modelo) {
        try {
            List<MotoDTO> motos = motoService.buscarPorModelo(modelo);
            log.info("Buscando motos do modelo: {}", modelo);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "motos", motos,
                "total", motos.size(),
                "modelo", modelo
            ));
        } catch (Exception e) {
            log.error("Erro ao buscar motos do modelo {}: {}", modelo, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
