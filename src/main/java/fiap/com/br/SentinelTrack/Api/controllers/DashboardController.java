package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.services.MotoService;
import fiap.com.br.SentinelTrack.Application.services.PatioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PatioService patioService;
    private final MotoService motoService;

    public DashboardController(PatioService patioService, MotoService motoService) {
        this.patioService = patioService;
        this.motoService = motoService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Estatísticas para o dashboard
        var patios = patioService.listarTodos();
        var motos = motoService.listarTodas();
        
        model.addAttribute("totalPatios", patios.size());
        model.addAttribute("totalMotos", motos.size());
        model.addAttribute("motosDisponiveis", 
            motos.stream().mapToInt(m -> "DISPONIVEL".equals(m.getStatus()) ? 1 : 0).sum());
        model.addAttribute("motosManutencao", 
            motos.stream().mapToInt(m -> "MANUTENCAO".equals(m.getStatus()) ? 1 : 0).sum());
        
        // Motos recentes (últimas 5)
        var motosRecentes = motos.stream()
            .sorted((m1, m2) -> m2.getDataEntrada().compareTo(m1.getDataEntrada()))
            .limit(5)
            .toList();
        model.addAttribute("motosRecentes", motosRecentes);
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
