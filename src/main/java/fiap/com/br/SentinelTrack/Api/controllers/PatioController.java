package fiap.com.br.SentinelTrack.Api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.services.PatioService;

@RestController
@RequestMapping("/api/patios")
public class PatioController {
    
    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public List<PatioDTO> listar() {
        return patioService.listar();
    }
}
