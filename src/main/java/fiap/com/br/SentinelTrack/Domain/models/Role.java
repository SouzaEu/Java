package fiap.com.br.SentinelTrack.Domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "ST_ROLE")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROLE")
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "NOME", nullable = false, unique = true)
    private String nome;
    
    @Column(name = "DESCRICAO")
    private String descricao;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
