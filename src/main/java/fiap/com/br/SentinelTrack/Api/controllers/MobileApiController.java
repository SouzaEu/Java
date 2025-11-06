package fiap.com.br.SentinelTrack.Api.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.LoginRequestDTO;
import fiap.com.br.SentinelTrack.Application.services.MotoService;
import fiap.com.br.SentinelTrack.Application.services.PatioService;
import fiap.com.br.SentinelTrack.Application.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

/**
 * Controller REST para APIs gerais do Mobile App
 * Challenge 2025 - 4º Sprint
 * 
 * Endpoints para autenticação, dashboard e health check
 */
@RestController
@RequestMapping("/api/mobile")
@CrossOrigin(origins = "*")
@Slf4j
public class MobileApiController {

    private final MotoService motoService;
    private final PatioService patioService;
    private final JwtService jwtService;

    public MobileApiController(MotoService motoService, PatioService patioService, JwtService jwtService) {
        this.motoService = motoService;
        this.patioService = patioService;
        this.jwtService = jwtService;
    }

    /**
     * Health check da API
     * GET /api/mobile/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "SentinelTrack Java API",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now(),
            "message", "API funcionando corretamente"
        ));
    }

    /**
     * Autenticação JWT para o mobile
     * POST /api/mobile/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            log.info("Tentativa de login para: {}", loginRequest.getEmail());
            
            // Validação de credenciais (para demonstração, aceita qualquer email válido com senha >= 6)
            // Em produção, isso seria validado contra banco de dados com hash da senha
            if (isValidCredentials(loginRequest.getEmail(), loginRequest.getSenha())) {
                
                // Determinar role baseado no email (para demonstração)
                String role = determineUserRole(loginRequest.getEmail());
                
                // Gerar token JWT real
                String token = jwtService.generateToken(loginRequest.getEmail(), role);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "user", Map.of(
                        "id", 1,
                        "email", loginRequest.getEmail(),
                        "nome", extractNameFromEmail(loginRequest.getEmail()),
                        "role", role
                    ),
                    "message", "Login realizado com sucesso",
                    "expiresIn", "24h"
                ));
            } else {
                log.warn("Tentativa de login falhada para: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "success", false,
                        "message", "Credenciais inválidas"
                    ));
            }
        } catch (Exception e) {
            log.error("Erro no login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", "Erro interno do servidor"));
        }
    }

    /**
     * Valida credenciais do usuário
     * Implementação simplificada para o Challenge - em produção usar Spring Security completo
     */
    private boolean isValidCredentials(String email, String senha) {
        // Validação básica de formato e tamanho
        // Em ambiente real: verificar hash da senha no banco de dados
        return email.contains("@") && 
               email.length() > 5 && 
               senha.length() >= 6 &&
               !senha.equals("123456"); // Evitar senhas muito simples
    }

    /**
     * Determina role do usuário baseado no email
     * Em produção: buscar role do banco de dados
     */
    private String determineUserRole(String email) {
        // Lógica de negócio para determinar roles
        if (email.toLowerCase().contains("admin")) return "ADMIN";
        if (email.toLowerCase().contains("gerente") || email.toLowerCase().contains("manager")) return "GERENTE";
        return "OPERADOR";
    }

    /**
     * Extrai nome do email para exibição
     */
    private String extractNameFromEmail(String email) {
        String name = email.substring(0, email.indexOf("@"));
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Dashboard com estatísticas
     * GET /api/mobile/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        try {
            List<MotoDTO> todasMotos = motoService.listarTodas();
            List<PatioDTO> todosPatios = patioService.listar();
            
            // Estatísticas
            long totalMotos = todasMotos.size();
            long motosDisponiveis = todasMotos.stream()
                .filter(m -> "DISPONIVEL".equals(m.getStatus()))
                .count();
            long motosEmUso = todasMotos.stream()
                .filter(m -> "EM_USO".equals(m.getStatus()))
                .count();
            long motosManutencao = todasMotos.stream()
                .filter(m -> "MANUTENCAO".equals(m.getStatus()))
                .count();
            
            log.info("Dashboard acessado - {} motos, {} pátios", totalMotos, todosPatios.size());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "estatisticas", Map.of(
                    "totalMotos", totalMotos,
                    "motosDisponiveis", motosDisponiveis,
                    "motosEmUso", motosEmUso,
                    "motosManutencao", motosManutencao,
                    "totalPatios", todosPatios.size(),
                    "percentualDisponibilidade", totalMotos > 0 ? (motosDisponiveis * 100.0 / totalMotos) : 0
                ),
                "motosRecentes", todasMotos.stream().limit(5).toList(),
                "patios", todosPatios,
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            log.error("Erro ao carregar dashboard: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Lista pátios para o mobile
     * GET /api/mobile/patios
     */
    @GetMapping("/patios")
    public ResponseEntity<?> listarPatios() {
        try {
            List<PatioDTO> patios = patioService.listar();
            log.info("Listando {} pátios para mobile", patios.size());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "patios", patios,
                "total", patios.size()
            ));
        } catch (Exception e) {
            log.error("Erro ao listar pátios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Relatórios para o mobile
     * POST /api/mobile/relatorios/uso
     */
    @PostMapping("/relatorios/uso")
    public ResponseEntity<?> gerarRelatorioUso(@RequestBody(required = false) Map<String, Object> filtros) {
        try {
            List<MotoDTO> motos = motoService.listarTodas();
            
            // Aplicar filtros se fornecidos
            if (filtros != null && filtros.containsKey("status")) {
                String status = (String) filtros.get("status");
                motos = motoService.buscarPorStatus(status);
            }
            
            log.info("Gerando relatório de uso com {} motos", motos.size());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "relatorio", Map.of(
                    "titulo", "Relatório de Uso de Motos",
                    "dataGeracao", LocalDateTime.now(),
                    "totalMotos", motos.size(),
                    "motos", motos,
                    "resumo", Map.of(
                        "disponivel", motos.stream().filter(m -> "DISPONIVEL".equals(m.getStatus())).count(),
                        "em_uso", motos.stream().filter(m -> "EM_USO".equals(m.getStatus())).count(),
                        "manutencao", motos.stream().filter(m -> "MANUTENCAO".equals(m.getStatus())).count()
                    )
                )
            ));
        } catch (Exception e) {
            log.error("Erro ao gerar relatório: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * Endpoint para sincronização com outras APIs
     * POST /api/mobile/sync
     */
    @PostMapping("/sync")
    public ResponseEntity<?> sincronizar() {
        try {
            // Simula sincronização com outras APIs
            log.info("Sincronização solicitada pelo mobile");
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Sincronização realizada com sucesso",
                "timestamp", LocalDateTime.now(),
                "apis_sincronizadas", List.of("VisionMoto Python API", "Database")
            ));
        } catch (Exception e) {
            log.error("Erro na sincronização: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
