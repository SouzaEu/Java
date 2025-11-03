package br.com.fiap.mottu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;

/**
 * DTO para Dispositivos IoT
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispositivoIotDTO {

    @NotBlank(message = "ID do dispositivo é obrigatório")
    private String idDispositivo;

    @NotBlank(message = "Nome do dispositivo é obrigatório")
    private String nomeDispositivo;

    @NotBlank(message = "Tipo do dispositivo é obrigatório")
    private String tipoDispositivo;

    private String status;

    private String descricaoLocalizacao;

    private LocalDateTime dataUltimaComunicacao;

    private String configuracao;

    private String codigoZona;

    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    // Campos calculados
    private Boolean online;
    private Boolean sensor;
    private Boolean atuador;
    private Long minutosOffline;

    // Para resposta da API
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Boolean success;
        private DispositivoIotDTO data;
        private String message;
        private LocalDateTime timestamp;
    }

    // Para listagem
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private Boolean success;
        private java.util.List<DispositivoIotDTO> dispositivos;
        private ResumoDTO resumo;
        private String message;
        private LocalDateTime timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResumoDTO {
        private Long total;
        private Long online;
        private Long offline;
        private Long sensores;
        private Long atuadores;
        private Long cameras;
    }

    // Para criação/atualização
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "ID do dispositivo é obrigatório")
        private String idDispositivo;

        @NotBlank(message = "Nome do dispositivo é obrigatório")
        private String nomeDispositivo;

        @NotBlank(message = "Tipo do dispositivo é obrigatório")
        private String tipoDispositivo;

        private String descricaoLocalizacao;

        private String configuracao;

        private String codigoZona;

        @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
        @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
        private Double latitude;

        @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
        @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
        private Double longitude;
    }

    // Para atualização de status
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdateRequest {
        private String status;
        private String configuracao;
        private Object dadosSensor; // JSON genérico
    }

    // Para eventos IoT
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventoIotRequest {
        @NotBlank(message = "ID do evento é obrigatório")
        private String idEvento;

        @NotBlank(message = "Tipo do evento é obrigatório")
        private String tipoEvento;

        private Object payload; // JSON genérico

        @lombok.Builder.Default
        private Boolean criarAlerta = true;

        @lombok.Builder.Default
        private String severidade = "MEDIUM";
    }

    // Para resposta de evento
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventoResponse {
        private String alertId;
        private String status;
        private Boolean idempotent;
        private LocalDateTime timestamp;
    }
}
