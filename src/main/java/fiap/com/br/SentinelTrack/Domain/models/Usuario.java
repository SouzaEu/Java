package fiap.com.br.SentinelTrack.Domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "ST_USUARIO")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;
    
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    
    @Column(name = "NOME_COMPLETO", nullable = false)
    private String nomeCompleto;
    
    @Column(name = "ATIVO")
    private Boolean ativo = true;
    
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ST_USUARIO_ROLE",
        joinColumns = @JoinColumn(name = "ID_USUARIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_ROLE")
    )
    private Set<Role> roles;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
