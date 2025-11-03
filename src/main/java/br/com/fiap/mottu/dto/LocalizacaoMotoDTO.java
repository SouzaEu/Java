package br.com.fiap.mottu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

/**
 * DTO para Localização das Motos
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizacaoMotoDTO {

    private Long idLocalizacao;

    @NotBlank(message = "Placa da moto é obrigatória")
    private String placaMoto;

    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;

    private String codigoZona;

    private String endereco;

    private String setor;

    private Integer andar;

    private String codigoVaga;

    private String descricaoLocalizacao;

    private String statusMoto;

    @Min(value = 0, message = "Nível de bateria deve ser entre 0 e 100")
    @Max(value = 100, message = "Nível de bateria deve ser entre 0 e 100")
    private Integer nivelBateria;

    private LocalDateTime dataUltimaAtualizacao;

    private String cpfUsuarioEmUso;

    // Campos calculados
    private String localizacaoCompleta;
    private String instrucoesLocalizacao;
    private Boolean disponivel;
    private Boolean emUso;
    private Boolean bateriaBaixa;
    private Boolean bateriaCritica;
    private String nomeUsuarioEmUso;

    // Para busca por placa (Mobile/Java/NET)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BuscaPorPlacaResponse {
        private Boolean success;
        private Boolean encontrada;
        private LocalizacaoMotoDTO moto;
        private LocalizacaoCompletaDTO localizacaoCompleta;
        private java.util.List<String> instrucoesLocalizacao;
        private String message;
        private LocalDateTime timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocalizacaoCompletaDTO {
        private String endereco;
        private String setor;
        private Integer andar;
        private String vaga;
        private String descricao;
        private CoordenadasDTO coordenadas;
        private String zona;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoordenadasDTO {
        private Double latitude;
        private Double longitude;
        // Para .NET
        private Double x;
        private Double y;
    }

    // Para listagem (Dashboard)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private Boolean success;
        private java.util.List<LocalizacaoMotoDTO> motos;
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
        private Long disponiveis;
        private Long emUso;
        private Long manutencao;
        private Long bateriaBaixa;
        private Double mediaBateria;
        private Long reservadas;
    }

    // Para atualização de localização
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AtualizacaoRequest {
        @NotBlank(message = "Placa da moto é obrigatória")
        private String placaMoto;

        private Double latitude;
        private Double longitude;
        private String codigoZona;
        private String endereco;
        private String setor;
        private Integer andar;
        private String codigoVaga;
        private String descricaoLocalizacao;

        @Min(value = 0, message = "Nível de bateria deve ser entre 0 e 100")
        @Max(value = 100, message = "Nível de bateria deve ser entre 0 e 100")
        private Integer nivelBateria;
    }

    // Para reserva de moto (Mobile)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservaRequest {
        @NotBlank(message = "CPF do usuário é obrigatório")
        private String cpfUsuario;

        private String observacoes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservaResponse {
        private Boolean success;
        private String motoId;
        private String status;
        private String message;
        private LocalDateTime timestamp;
    }

    // Para dashboard por zona
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DashboardZonaDTO {
        private String zona;
        private Long totalMotos;
        private Long disponiveis;
        private Long emUso;
        private Long manutencao;
        private Double mediaBateria;
        private LocalDateTime ultimaAtualizacao;
    }

    // Para relatórios
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RelatorioUsoDTO {
        private String placaMoto;
        private String cpfUsuario;
        private String nomeUsuario;
        private LocalDateTime inicioUso;
        private LocalDateTime fimUso;
        private Integer tempoUsoMinutos;
        private Double distanciaKm;
        private String localizacaoInicial;
        private String localizacaoFinal;
        private String status;
    }

    // Para integração .NET
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DotNetResponse {
        private Boolean isSuccess;
        private DotNetDataDTO data;
        private String message;
        private LocalDateTime timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DotNetDataDTO {
        private java.util.List<DotNetMotorcycleDTO> motorcycles;
        private DotNetSummaryDTO summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DotNetMotorcycleDTO {
        private String id;
        private String model;
        private String licensePlate;
        private String status;
        private Integer batteryLevel;
        private Double locationX;
        private Double locationY;
        private String zone;
        private String address;
        private String sector;
        private Integer floor;
        private String parkingSpot;
        private String locationDescription;
        private LocalDateTime lastUpdate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DotNetSummaryDTO {
        private Long totalCount;
        private Long availableCount;
        private Long inUseCount;
        private Long maintenanceCount;
    }
}
