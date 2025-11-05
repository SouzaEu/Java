package fiap.com.br.SentinelTrack.Api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fiap.com.br.SentinelTrack.Application.services.PatioService;
import fiap.com.br.SentinelTrack.Domain.models.Patio;

@RestController
@RequestMapping("/patios")
public class PatioController {
    
    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public List<Patio> listar() {
        return patioService.listar();
    }
}
