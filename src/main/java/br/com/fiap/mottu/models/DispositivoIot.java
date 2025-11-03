package br.com.fiap.mottu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade para Dispositivos IoT
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Entity
@Table(name = "T_MT_DISPOSITIVO_IOT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIot {

    @Id
    @Column(name = "ID_DISPOSITIVO", length = 50)
    @NotBlank(message = "ID do dispositivo é obrigatório")
    private String idDispositivo;

    @Column(name = "NM_DISPOSITIVO", length = 100, nullable = false)
    @NotBlank(message = "Nome do dispositivo é obrigatório")
    private String nomeDispositivo;

    @Column(name = "TP_DISPOSITIVO", length = 50, nullable = false)
    @NotBlank(message = "Tipo do dispositivo é obrigatório")
    private String tipoDispositivo;

    @Column(name = "ST_STATUS", length = 20)
    private String status = "ONLINE";

    @Column(name = "DS_LOCALIZACAO", length = 200)
    private String descricaoLocalizacao;

    @Column(name = "DT_ULTIMA_COM")
    private LocalDateTime dataUltimaComunicacao;

    @Lob
    @Column(name = "TX_CONFIGURACAO")
    private String configuracao;

    @Column(name = "CD_ZONA", length = 10)
    private String codigoZona;

    @Column(name = "NR_LATITUDE", precision = 10, scale = 7)
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;

    @Column(name = "NR_LONGITUDE", precision = 10, scale = 7)
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;

    @Column(name = "DT_CRIACAO")
    private LocalDateTime dataCriacao;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (dataUltimaComunicacao == null) {
            dataUltimaComunicacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // Enums para validação
    public enum TipoDispositivo {
        SENSOR_MOVIMENTO("sensor_movimento"),
        CAMERA("camera"),
        ATUADOR_TRAVA("atuador_trava"),
        ATUADOR_ALARME("atuador_alarme"),
        SENSOR_TEMPERATURA("sensor_temperatura"),
        SENSOR_BATERIA("sensor_bateria");

        private final String valor;

        TipoDispositivo(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public enum StatusDispositivo {
        ONLINE("ONLINE"),
        OFFLINE("OFFLINE"),
        MANUTENCAO("MANUTENCAO"),
        ERRO("ERRO");

        private final String valor;

        StatusDispositivo(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    // Métodos utilitários
    public boolean isOnline() {
        return "ONLINE".equals(this.status);
    }

    public boolean isSensor() {
        return tipoDispositivo != null && tipoDispositivo.startsWith("sensor_");
    }

    public boolean isAtuador() {
        return tipoDispositivo != null && tipoDispositivo.startsWith("atuador_");
    }
}
