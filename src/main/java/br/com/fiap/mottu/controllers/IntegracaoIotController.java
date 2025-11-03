package br.com.fiap.mottu.controllers;

import br.com.fiap.mottu.dto.DispositivoIotDTO;
import br.com.fiap.mottu.dto.LocalizacaoMotoDTO;
import br.com.fiap.mottu.models.LocalizacaoMoto;
import br.com.fiap.mottu.repositories.LocalizacaoMotoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller para Integração IoT - APIs Multi-disciplinares
 * Challenge 2025 - 4º Sprint
 * 
 * Endpoints compatíveis com:
 * - Mobile App (React Native)
 * - Java/Spring Boot
 * - .NET Core
 * - VisionMoto Python API
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class IntegracaoIotController {

    @Autowired
    private LocalizacaoMotoRepository localizacaoMotoRepository;

    @Autowired
    private EntityManager entityManager;

    // ================================
    // ENDPOINTS MOBILE APP
    // ================================

    @GetMapping("/mobile/motos")
    public ResponseEntity<LocalizacaoMotoDTO.ListResponse> mobileListarMotos(
            @RequestParam(defaultValue = "DISPONIVEL") String status) {
        
        try {
            List<LocalizacaoMoto> motos = status.equals("ALL") 
                ? localizacaoMotoRepository.findAll()
                : localizacaoMotoRepository.findByStatusMoto(status);

            List<LocalizacaoMotoDTO> motosDTO = motos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

            // Calcula resumo
            Object[] resumoArray = localizacaoMotoRepository.getResumoGeral();
            LocalizacaoMotoDTO.ResumoDTO resumo = LocalizacaoMotoDTO.ResumoDTO.builder()
                .total((long) motosDTO.size())
                .disponiveis(((Number) resumoArray[0]).longValue())
                .emUso(((Number) resumoArray[1]).longValue())
                .manutencao(((Number) resumoArray[2]).longValue())
                .bateriaBaixa(((Number) resumoArray[3]).longValue())
                .mediaBateria(((Number) resumoArray[4]).doubleValue())
                .build();

            LocalizacaoMotoDTO.ListResponse response = LocalizacaoMotoDTO.ListResponse.builder()
                .success(true)
                .motos(motosDTO)
                .resumo(resumo)
                .message("Motos listadas com sucesso")
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LocalizacaoMotoDTO.ListResponse errorResponse = LocalizacaoMotoDTO.ListResponse.builder()
                .success(false)
                .message("Erro ao listar motos: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/mobile/motos/buscar/{placa}")
    public ResponseEntity<LocalizacaoMotoDTO.BuscaPorPlacaResponse> mobileBuscarMotoPorPlaca(
            @PathVariable String placa) {
        
        try {
            Optional<LocalizacaoMoto> motoOpt = localizacaoMotoRepository.findByPlacaMoto(placa.toUpperCase());

            if (motoOpt.isEmpty()) {
                LocalizacaoMotoDTO.BuscaPorPlacaResponse response = LocalizacaoMotoDTO.BuscaPorPlacaResponse.builder()
                    .success(false)
                    .encontrada(false)
                    .message("Moto com placa " + placa + " não encontrada")
                    .timestamp(LocalDateTime.now())
                    .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            LocalizacaoMoto moto = motoOpt.get();
            LocalizacaoMotoDTO motoDTO = convertToDTO(moto);

            // Monta localização completa
            LocalizacaoMotoDTO.LocalizacaoCompletaDTO localizacaoCompleta = LocalizacaoMotoDTO.LocalizacaoCompletaDTO.builder()
                .endereco(moto.getEndereco())
                .setor(moto.getSetor())
                .andar(moto.getAndar())
                .vaga(moto.getCodigoVaga())
                .descricao(moto.getDescricaoLocalizacao())
                .coordenadas(LocalizacaoMotoDTO.CoordenadasDTO.builder()
                    .latitude(moto.getLatitude())
                    .longitude(moto.getLongitude())
                    .build())
                .zona(moto.getCodigoZona())
                .build();

            // Instruções de localização
            List<String> instrucoes = List.of(moto.getInstrucoesLocalizacao().split("\n"));

            LocalizacaoMotoDTO.BuscaPorPlacaResponse response = LocalizacaoMotoDTO.BuscaPorPlacaResponse.builder()
                .success(true)
                .encontrada(true)
                .moto(motoDTO)
                .localizacaoCompleta(localizacaoCompleta)
                .instrucoesLocalizacao(instrucoes)
                .message("Moto encontrada com sucesso")
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LocalizacaoMotoDTO.BuscaPorPlacaResponse errorResponse = LocalizacaoMotoDTO.BuscaPorPlacaResponse.builder()
                .success(false)
                .encontrada(false)
                .message("Erro ao buscar moto: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/mobile/motos/{placaMoto}/reservar")
    public ResponseEntity<LocalizacaoMotoDTO.ReservaResponse> mobileReservarMoto(
            @PathVariable String placaMoto,
            @Valid @RequestBody LocalizacaoMotoDTO.ReservaRequest request) {
        
        try {
            // Chama procedure para iniciar uso
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_INICIAR_USO_MOTO");
            query.registerStoredProcedureParameter("p_placa", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_cpf_usuario", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_localizacao_inicial", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", String.class, ParameterMode.OUT);

            query.setParameter("p_placa", placaMoto.toUpperCase());
            query.setParameter("p_cpf_usuario", request.getCpfUsuario());
            query.setParameter("p_localizacao_inicial", "Reserva via Mobile App");

            query.execute();

            String resultado = (String) query.getOutputParameterValue("p_resultado");

            if (resultado.startsWith("SUCCESS")) {
                LocalizacaoMotoDTO.ReservaResponse response = LocalizacaoMotoDTO.ReservaResponse.builder()
                    .success(true)
                    .motoId(placaMoto)
                    .status("RESERVADA")
                    .message("Moto reservada com sucesso")
                    .timestamp(LocalDateTime.now())
                    .build();

                return ResponseEntity.ok(response);
            } else {
                LocalizacaoMotoDTO.ReservaResponse response = LocalizacaoMotoDTO.ReservaResponse.builder()
                    .success(false)
                    .message("Erro ao reservar moto: " + resultado)
                    .timestamp(LocalDateTime.now())
                    .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            LocalizacaoMotoDTO.ReservaResponse errorResponse = LocalizacaoMotoDTO.ReservaResponse.builder()
                .success(false)
                .message("Erro interno: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // ENDPOINTS JAVA/SPRING BOOT
    // ================================

    @GetMapping("/java/motos/status")
    public ResponseEntity<Object> javaMotosStatus() {
        try {
            List<LocalizacaoMoto> motos = localizacaoMotoRepository.findAll();

            List<Object> motosJava = motos.stream()
                .map(this::convertToJavaFormat)
                .collect(Collectors.toList());

            Object[] resumoArray = localizacaoMotoRepository.getResumoGeral();

            Object resumo = java.util.Map.of(
                "total", motosJava.size(),
                "disponiveis", ((Number) resumoArray[0]).longValue(),
                "emUso", ((Number) resumoArray[1]).longValue(),
                "manutencao", ((Number) resumoArray[2]).longValue()
            );

            Object response = java.util.Map.of(
                "success", true,
                "data", java.util.Map.of(
                    "motos", motosJava,
                    "resumo", resumo
                ),
                "timestamp", LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Object errorResponse = java.util.Map.of(
                "success", false,
                "error", e.getMessage(),
                "timestamp", LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/java/motos/buscar/{placa}")
    public ResponseEntity<Object> javaBuscarMotoPorPlaca(@PathVariable String placa) {
        try {
            Optional<LocalizacaoMoto> motoOpt = localizacaoMotoRepository.findByPlacaMoto(placa.toUpperCase());

            if (motoOpt.isEmpty()) {
                Object response = java.util.Map.of(
                    "success", false,
                    "error", "Moto com placa " + placa + " não encontrada",
                    "timestamp", LocalDateTime.now()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            LocalizacaoMoto moto = motoOpt.get();
            Object motoJava = convertToJavaFormat(moto);

            Object response = java.util.Map.of(
                "success", true,
                "data", java.util.Map.of(
                    "moto", motoJava,
                    "encontrada", true
                ),
                "timestamp", LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Object errorResponse = java.util.Map.of(
                "success", false,
                "error", e.getMessage(),
                "timestamp", LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // ENDPOINTS .NET CORE
    // ================================

    @GetMapping("/dotnet/Dashboard/GetMotorcycleData")
    public ResponseEntity<LocalizacaoMotoDTO.DotNetResponse> dotnetGetMotorcycleData() {
        try {
            List<LocalizacaoMoto> motos = localizacaoMotoRepository.findAll();

            List<LocalizacaoMotoDTO.DotNetMotorcycleDTO> motorcycles = motos.stream()
                .map(this::convertToDotNetFormat)
                .collect(Collectors.toList());

            Object[] resumoArray = localizacaoMotoRepository.getResumoGeral();

            LocalizacaoMotoDTO.DotNetSummaryDTO summary = LocalizacaoMotoDTO.DotNetSummaryDTO.builder()
                .totalCount((long) motorcycles.size())
                .availableCount(((Number) resumoArray[0]).longValue())
                .inUseCount(((Number) resumoArray[1]).longValue())
                .maintenanceCount(((Number) resumoArray[2]).longValue())
                .build();

            LocalizacaoMotoDTO.DotNetDataDTO data = LocalizacaoMotoDTO.DotNetDataDTO.builder()
                .motorcycles(motorcycles)
                .summary(summary)
                .build();

            LocalizacaoMotoDTO.DotNetResponse response = LocalizacaoMotoDTO.DotNetResponse.builder()
                .isSuccess(true)
                .data(data)
                .message("Data retrieved successfully")
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LocalizacaoMotoDTO.DotNetResponse errorResponse = LocalizacaoMotoDTO.DotNetResponse.builder()
                .isSuccess(false)
                .message("Failed to retrieve motorcycle data: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/dotnet/Motorcycles/FindByPlate/{placa}")
    public ResponseEntity<Object> dotnetFindByPlate(@PathVariable String placa) {
        try {
            Optional<LocalizacaoMoto> motoOpt = localizacaoMotoRepository.findByPlacaMoto(placa.toUpperCase());

            if (motoOpt.isEmpty()) {
                Object response = java.util.Map.of(
                    "IsSuccess", false,
                    "Error", "Motorcycle with plate " + placa + " not found",
                    "Message", "Motorcycle not found in database",
                    "Timestamp", LocalDateTime.now()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            LocalizacaoMoto moto = motoOpt.get();
            LocalizacaoMotoDTO.DotNetMotorcycleDTO motorcycle = convertToDotNetFormat(moto);

            Object response = java.util.Map.of(
                "IsSuccess", true,
                "Data", java.util.Map.of(
                    "Motorcycle", motorcycle,
                    "Found", true
                ),
                "Message", "Motorcycle found successfully",
                "Timestamp", LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Object errorResponse = java.util.Map.of(
                "IsSuccess", false,
                "Error", e.getMessage(),
                "Message", "Failed to find motorcycle",
                "Timestamp", LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // ENDPOINTS IOT
    // ================================

    @PostMapping("/iot/eventos")
    public ResponseEntity<DispositivoIotDTO.EventoResponse> iotReceberEvento(
            @Valid @RequestBody DispositivoIotDTO.EventoIotRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        
        try {
            String eventoId = idempotencyKey != null ? idempotencyKey : request.getIdEvento();

            // Chama procedure para registrar evento IoT
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_REGISTRAR_EVENTO_IOT");
            query.registerStoredProcedureParameter("p_id_evento", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_dispositivo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_tipo_evento", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_payload", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_criar_alerta", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_resultado", String.class, ParameterMode.OUT);

            query.setParameter("p_id_evento", eventoId);
            query.setParameter("p_id_dispositivo", "UNKNOWN"); // Será extraído do payload
            query.setParameter("p_tipo_evento", request.getTipoEvento());
            query.setParameter("p_payload", request.getPayload().toString());
            query.setParameter("p_criar_alerta", request.getCriarAlerta() ? 1 : 0);

            query.execute();

            String resultado = (String) query.getOutputParameterValue("p_resultado");

            if (resultado.startsWith("SUCCESS") || resultado.startsWith("WARNING")) {
                String alertId = resultado.contains(":") ? resultado.split(":")[1] : null;
                boolean idempotent = resultado.startsWith("WARNING");

                DispositivoIotDTO.EventoResponse response = DispositivoIotDTO.EventoResponse.builder()
                    .alertId(alertId)
                    .status("OPEN")
                    .idempotent(idempotent)
                    .timestamp(LocalDateTime.now())
                    .build();

                return ResponseEntity.status(idempotent ? HttpStatus.OK : HttpStatus.CREATED).body(response);
            } else {
                DispositivoIotDTO.EventoResponse response = DispositivoIotDTO.EventoResponse.builder()
                    .status("ERROR")
                    .timestamp(LocalDateTime.now())
                    .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            DispositivoIotDTO.EventoResponse errorResponse = DispositivoIotDTO.EventoResponse.builder()
                .status("ERROR")
                .timestamp(LocalDateTime.now())
                .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================

    private LocalizacaoMotoDTO convertToDTO(LocalizacaoMoto moto) {
        return LocalizacaoMotoDTO.builder()
            .idLocalizacao(moto.getIdLocalizacao())
            .placaMoto(moto.getPlacaMoto())
            .latitude(moto.getLatitude())
            .longitude(moto.getLongitude())
            .codigoZona(moto.getCodigoZona())
            .endereco(moto.getEndereco())
            .setor(moto.getSetor())
            .andar(moto.getAndar())
            .codigoVaga(moto.getCodigoVaga())
            .descricaoLocalizacao(moto.getDescricaoLocalizacao())
            .statusMoto(moto.getStatusMoto())
            .nivelBateria(moto.getNivelBateria())
            .dataUltimaAtualizacao(moto.getDataUltimaAtualizacao())
            .cpfUsuarioEmUso(moto.getCpfUsuarioEmUso())
            .localizacaoCompleta(moto.getLocalizacaoCompleta())
            .instrucoesLocalizacao(moto.getInstrucoesLocalizacao())
            .disponivel(moto.isDisponivel())
            .emUso(moto.isEmUso())
            .bateriaBaixa(moto.isBateriaBaixa())
            .bateriaCritica(moto.isBateriaCritica())
            .build();
    }

    private Object convertToJavaFormat(LocalizacaoMoto moto) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("motoId", moto.getPlacaMoto());
        map.put("modelo", "N/A"); // Seria necessário join com tabela moto
        map.put("placa", moto.getPlacaMoto());
        map.put("status", moto.getStatusMoto());
        map.put("nivelBateria", moto.getNivelBateria() != null ? moto.getNivelBateria() : 0);
        map.put("latitude", moto.getLatitude() != null ? moto.getLatitude() : 0.0);
        map.put("longitude", moto.getLongitude() != null ? moto.getLongitude() : 0.0);
        map.put("zona", moto.getCodigoZona() != null ? moto.getCodigoZona() : "");
        map.put("endereco", moto.getEndereco() != null ? moto.getEndereco() : "");
        map.put("setor", moto.getSetor() != null ? moto.getSetor() : "");
        map.put("andar", moto.getAndar() != null ? moto.getAndar() : 1);
        map.put("vaga", moto.getCodigoVaga() != null ? moto.getCodigoVaga() : "");
        map.put("descricaoLocalizacao", moto.getDescricaoLocalizacao() != null ? moto.getDescricaoLocalizacao() : "");
        map.put("ultimaAtualizacao", moto.getDataUltimaAtualizacao());
        return map;
    }

    private LocalizacaoMotoDTO.DotNetMotorcycleDTO convertToDotNetFormat(LocalizacaoMoto moto) {
        return LocalizacaoMotoDTO.DotNetMotorcycleDTO.builder()
            .id(moto.getPlacaMoto())
            .model("N/A") // Seria necessário join com tabela moto
            .licensePlate(moto.getPlacaMoto())
            .status(moto.getStatusMoto())
            .batteryLevel(moto.getNivelBateria() != null ? moto.getNivelBateria() : 0)
            .locationX(moto.getLatitude() != null ? moto.getLatitude() : 0.0)
            .locationY(moto.getLongitude() != null ? moto.getLongitude() : 0.0)
            .zone(moto.getCodigoZona() != null ? moto.getCodigoZona() : "")
            .address(moto.getEndereco() != null ? moto.getEndereco() : "")
            .sector(moto.getSetor() != null ? moto.getSetor() : "")
            .floor(moto.getAndar() != null ? moto.getAndar() : 1)
            .parkingSpot(moto.getCodigoVaga() != null ? moto.getCodigoVaga() : "")
            .locationDescription(moto.getDescricaoLocalizacao() != null ? moto.getDescricaoLocalizacao() : "")
            .lastUpdate(moto.getDataUltimaAtualizacao())
            .build();
    }
}
