package fiap.com.br.SentinelTrack.Domain.models;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "patio")
@Table(name = "ST_PATIO")
@Data
public class Patio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PATIO")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "ENDERECO")
    private String endereco;

    @Column(name = "COMPLEMENTO")
    private String complemento;

    @Column(name = "AREA_M2", precision = 10, scale = 2)
    private BigDecimal areaM2;

    @Column(name = "ID_LOCALIDADE", nullable = false)
    private Long idLocalidade;

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Moto> motos;
}
