package fiap.com.br.SentinelTrack.Infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Recursos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Para desenvolvimento
                .requestMatchers("/login", "/error").permitAll()
                
                // Swagger/OpenAPI - Para desenvolvimento
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // APIs REST para Mobile App - Challenge 2025
                .requestMatchers("/api/**").permitAll()
                
                // Rotas protegidas por perfil
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/gerente/**").hasAnyRole("ADMIN", "GERENTE")
                .requestMatchers("/patios/new", "/patios/edit/**", "/patios/delete/**").hasAnyRole("ADMIN", "GERENTE")
                .requestMatchers("/motos/new", "/motos/edit/**", "/motos/delete/**").hasAnyRole("ADMIN", "GERENTE", "OPERADOR")
                
                // Demais rotas requerem autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        // Para H2 Console e APIs REST (apenas desenvolvimento)
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"));
        http.headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }
}
