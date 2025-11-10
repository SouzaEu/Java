package fiap.com.br.SentinelTrack.Infrastructure.config;

import fiap.com.br.SentinelTrack.Domain.models.Role;
import fiap.com.br.SentinelTrack.Domain.models.Usuario;
import fiap.com.br.SentinelTrack.Domain.repositories.RoleRepository;
import fiap.com.br.SentinelTrack.Domain.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, 
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se já existe o usuário admin
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                // Cria role ADMIN
                Role adminRole = new Role();
                adminRole.setNome("ROLE_ADMIN");
                adminRole.setDescricao("Administrador do sistema");
                adminRole.setCreatedAt(LocalDateTime.now());
                adminRole = roleRepository.save(adminRole);

                // Cria usuário admin
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@sentineltrack.com");
                admin.setNomeCompleto("Administrador");
                admin.setAtivo(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);

                usuarioRepository.save(admin);
                
                System.out.println("✅ Usuário admin criado com sucesso!");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            }
        };
    }
}
