package br.com.fiap.mottu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade para Sistema de Alertas
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Entity
@Table(name = "T_MT_ALERTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @Column(name = "ID_ALERTA", length = 50)
    @NotBlank(message = "ID do alerta é obrigatório")
    private String idAlerta;

    @Column(name = "TP_ALERTA", length = 50, nullable = false)
    @NotBlank(message = "Tipo do alerta é obrigatório")
    private String tipoAlerta;

    @Column(name = "NV_SEVERIDADE", length = 20)
    private String nivelSeveridade = "INFO";

    @Column(name = "TX_TITULO", length = 200, nullable = false)
    @NotBlank(message = "Título do alerta é obrigatório")
    private String titulo;

    @Lob
    @Column(name = "TX_DESCRICAO")
    private String descricao;

    @Column(name = "CD_PLACA_MOTO", length = 10)
    private String placaMoto;

    @Column(name = "ID_DISPOSITIVO", length = 50)
    private String idDispositivo;

    @Column(name = "CD_ZONA", length = 10)
    private String codigoZona;

    @Column(name = "ST_ATIVO", length = 1)
    private String ativo = "S";

    @Column(name = "DT_CRIACAO")
    private LocalDateTime dataCriacao;

    @Column(name = "DT_RESOLUCAO")
    private LocalDateTime dataResolucao;

    @Column(name = "ID_RESOLVIDO_POR")
    private Long idResolvidoPor;

    @Lob
    @Column(name = "TX_OBSERVACOES")
    private String observacoes;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PLACA_MOTO", referencedColumnName = "CD_PLACA", insertable = false, updatable = false)
    private Moto moto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DISPOSITIVO", referencedColumnName = "ID_DISPOSITIVO", insertable = false, updatable = false)
    private DispositivoIot dispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RESOLVIDO_POR", referencedColumnName = "ID_FUNCIONARIO", insertable = false, updatable = false)
    private Funcionario resolvidoPor;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (idAlerta == null || idAlerta.isEmpty()) {
            idAlerta = gerarIdAlerta();
        }
    }

    // Enums para validação
    public enum TipoAlerta {
        IOT("iot"),
        SISTEMA("sistema"),
        MANUTENCAO("manutencao"),
        SEGURANCA("seguranca"),
        BATERIA("bateria"),
        MOVIMENTO("movimento");

        private final String valor;

        TipoAlerta(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public enum NivelSeveridade {
        LOW("LOW"),
        MEDIUM("MEDIUM"),
        HIGH("HIGH"),
        CRITICAL("CRITICAL");

        private final String valor;

        NivelSeveridade(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    // Métodos utilitários
    public boolean isAtivo() {
        return "S".equals(this.ativo);
    }

    public boolean isResolvido() {
        return dataResolucao != null;
    }

    public boolean isCritico() {
        return "CRITICAL".equals(this.nivelSeveridade);
    }

    public boolean isAlto() {
        return "HIGH".equals(this.nivelSeveridade);
    }

    public void resolver(Long funcionarioId, String observacoes) {
        this.ativo = "N";
        this.dataResolucao = LocalDateTime.now();
        this.idResolvidoPor = funcionarioId;
        this.observacoes = observacoes;
    }

    public String getStatusFormatado() {
        if (isResolvido()) {
            return "RESOLVIDO";
        } else if (isAtivo()) {
            return "ATIVO";
        } else {
            return "INATIVO";
        }
    }

    private String gerarIdAlerta() {
        return "ALR-" + java.time.LocalDate.now().toString().replace("-", "") + "-" + 
               java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public long getTempoAbertoEmMinutos() {
        if (dataCriacao == null) return 0;
        
        LocalDateTime fim = dataResolucao != null ? dataResolucao : LocalDateTime.now();
        return java.time.Duration.between(dataCriacao, fim).toMinutes();
    }
}
