package br.com.fiap.mottu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade para Localização das Motos em Tempo Real
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Entity
@Table(name = "T_MT_LOCALIZACAO_MOTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizacaoMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOCALIZACAO")
    private Long idLocalizacao;

    @Column(name = "CD_PLACA", length = 10, nullable = false, unique = true)
    @NotBlank(message = "Placa da moto é obrigatória")
    private String placaMoto;

    @Column(name = "NR_LATITUDE", precision = 10, scale = 7)
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;

    @Column(name = "NR_LONGITUDE", precision = 10, scale = 7)
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;

    @Column(name = "CD_ZONA", length = 10)
    private String codigoZona;

    @Column(name = "DS_ENDERECO", length = 300)
    private String endereco;

    @Column(name = "DS_SETOR", length = 50)
    private String setor;

    @Column(name = "NR_ANDAR")
    private Integer andar = 1;

    @Column(name = "CD_VAGA", length = 20)
    private String codigoVaga;

    @Column(name = "DS_DESCRICAO_LOC", length = 500)
    private String descricaoLocalizacao;

    @Column(name = "ST_STATUS_MOTO", length = 20)
    private String statusMoto = "DISPONIVEL";

    @Column(name = "NR_BATERIA")
    @Min(value = 0, message = "Nível de bateria deve ser entre 0 e 100")
    @Max(value = 100, message = "Nível de bateria deve ser entre 0 e 100")
    private Integer nivelBateria = 100;

    @Column(name = "DT_ULTIMA_ATU")
    private LocalDateTime dataUltimaAtualizacao;

    @Column(name = "CD_CPF_EM_USO", length = 20)
    private String cpfUsuarioEmUso;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PLACA", referencedColumnName = "CD_PLACA", insertable = false, updatable = false)
    private Moto moto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_CPF_EM_USO", referencedColumnName = "CD_CPF", insertable = false, updatable = false)
    private Usuario usuarioEmUso;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        dataUltimaAtualizacao = LocalDateTime.now();
    }

    // Enums para validação
    public enum StatusMoto {
        DISPONIVEL("DISPONIVEL"),
        EM_USO("EM_USO"),
        MANUTENCAO("MANUTENCAO"),
        RESERVADA("RESERVADA"),
        INATIVA("INATIVA");

        private final String valor;

        StatusMoto(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    // Métodos utilitários
    public boolean isDisponivel() {
        return "DISPONIVEL".equals(this.statusMoto);
    }

    public boolean isEmUso() {
        return "EM_USO".equals(this.statusMoto);
    }

    public boolean isBateriaBaixa() {
        return nivelBateria != null && nivelBateria < 20;
    }

    public boolean isBateriaCritica() {
        return nivelBateria != null && nivelBateria < 10;
    }

    public String getLocalizacaoCompleta() {
        StringBuilder sb = new StringBuilder();
        
        if (endereco != null && !endereco.isEmpty()) {
            sb.append(endereco);
        }
        
        if (setor != null && !setor.isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(setor);
        }
        
        if (andar != null && andar > 0) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(andar).append("º andar");
        }
        
        if (codigoVaga != null && !codigoVaga.isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append("Vaga ").append(codigoVaga);
        }
        
        return sb.toString();
    }

    public String getInstrucoesLocalizacao() {
        StringBuilder sb = new StringBuilder();
        sb.append("Para encontrar a moto ").append(placaMoto).append(":\n");
        
        if (endereco != null && !endereco.isEmpty()) {
            sb.append("📍 Endereço: ").append(endereco).append("\n");
        }
        
        if (setor != null && !setor.isEmpty()) {
            sb.append("🏢 ").append(setor);
            if (andar != null && andar > 0) {
                sb.append(" - ").append(andar).append("º andar");
            }
            sb.append("\n");
        }
        
        if (codigoVaga != null && !codigoVaga.isEmpty()) {
            sb.append("🅿️ Vaga: ").append(codigoVaga).append("\n");
        }
        
        if (descricaoLocalizacao != null && !descricaoLocalizacao.isEmpty()) {
            sb.append("ℹ️ ").append(descricaoLocalizacao);
        }
        
        return sb.toString();
    }
}
