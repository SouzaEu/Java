package fiap.com.br.SentinelTrack.Domain.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity(name = "moto")
@Table(name = "ST_MOTO")
@Data
public class Moto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID_MOTO")
        private Long id;

        @Column(name = "MODELO", nullable = false)
        private String modelo;

        @Id
        @Column(name = "PLACA", nullable = false, unique = true)
        @Pattern(
                regexp = "^([A-Z]{3}[0-9]{4}|[A-Z]{3}[0-9][A-Z][0-9]{2})$",
                message = "Tipo de placa não suportada."
        )
        private String placa;

        @Column(name = "STATUS", nullable = false)
        private String status;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ID_PATIO", nullable = false)
        @JsonBackReference
        private Patio patio;

        @Column(name = "DATA_ENTRADA", nullable = false)
        @Temporal(TemporalType.DATE)
        private Date dataEntrada;
}
