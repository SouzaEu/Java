package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.services.PatioServiceV2;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller profissional para páginas web de pátios
 * Implementa Single Responsibility Principle - apenas controla fluxo web
 */
@Controller
@RequestMapping("/patios")
@Slf4j
public class PatioWebControllerV2 {

    private final PatioServiceV2 patioService;

    public PatioWebControllerV2(PatioServiceV2 patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        log.debug("Listando pátios. Busca: {}", busca);
        
        var patios = busca != null && !busca.trim().isEmpty() 
            ? patioService.buscarPorNome(busca.trim())
            : patioService.listarTodos();
        
        model.addAttribute("patios", patios);
        model.addAttribute("busca", busca);
        return "patios/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String novoForm(Model model) {
        model.addAttribute("patio", new CreatePatioDTO());
        model.addAttribute("isEdit", false);
        return "patios/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String criar(@Valid @ModelAttribute("patio") CreatePatioDTO patio, 
                       BindingResult result, 
                       RedirectAttributes redirectAttributes,
                       Model model) {
        
        if (result.hasErrors()) {
            log.warn("Erro de validação ao criar pátio: {}", result.getAllErrors());
            model.addAttribute("isEdit", false);
            return "patios/form";
        }

        patioService.criar(patio);
        redirectAttributes.addFlashAttribute("success", "Pátio criado com sucesso!");
        return "redirect:/patios";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String editarForm(@PathVariable Long id, Model model) {
        var patio = patioService.buscarPorId(id);
        
        var updateDTO = new UpdatePatioDTO();
        updateDTO.setNome(patio.getNome());
        updateDTO.setEndereco(patio.getEndereco());
        updateDTO.setComplemento(patio.getComplemento());
        updateDTO.setAreaM2(patio.getAreaM2());
        updateDTO.setIdLocalidade(patio.getIdLocalidade());

        model.addAttribute("patio", updateDTO);
        model.addAttribute("patioId", id);
        model.addAttribute("isEdit", true);
        return "patios/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String atualizar(@PathVariable Long id,
                           @Valid @ModelAttribute("patio") UpdatePatioDTO patio,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (result.hasErrors()) {
            log.warn("Erro de validação ao atualizar pátio ID {}: {}", id, result.getAllErrors());
            model.addAttribute("patioId", id);
            model.addAttribute("isEdit", true);
            return "patios/form";
        }

        patioService.atualizar(id, patio);
        redirectAttributes.addFlashAttribute("success", "Pátio atualizado com sucesso!");
        return "redirect:/patios";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        patioService.deletar(id);
        redirectAttributes.addFlashAttribute("success", "Pátio removido com sucesso!");
        return "redirect:/patios";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        var patio = patioService.buscarPorId(id);
        model.addAttribute("patio", patio);
        return "patios/details";
    }
}
