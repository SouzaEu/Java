package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.services.FixedPatioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller CORRIGIDO para páginas web de pátios
 * Resolve todos os problemas de compilação e imports
 */
@Controller
@RequestMapping("/patios-fixed")
@Slf4j
public class FixedPatioController {

    private final FixedPatioService patioService;

    public FixedPatioController(@Qualifier("fixedPatioService") FixedPatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        log.debug("Listando pátios. Busca: {}", busca);
        
        try {
            var patios = busca != null && !busca.trim().isEmpty() 
                ? patioService.buscarPorNome(busca.trim())
                : patioService.listarTodos();
            
            model.addAttribute("patios", patios);
            model.addAttribute("busca", busca);
            return "patios/list";
        } catch (Exception e) {
            log.error("Erro ao listar pátios", e);
            model.addAttribute("error", "Erro ao carregar pátios");
            return "error";
        }
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

        try {
            patioService.criar(patio);
            redirectAttributes.addFlashAttribute("success", "Pátio criado com sucesso!");
            return "redirect:/patios-fixed";
        } catch (Exception e) {
            log.error("Erro ao criar pátio", e);
            model.addAttribute("error", "Erro ao criar pátio");
            model.addAttribute("isEdit", false);
            return "patios/form";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
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
        } catch (Exception e) {
            log.error("Erro ao carregar pátio para edição", e);
            redirectAttributes.addFlashAttribute("error", "Pátio não encontrado");
            return "redirect:/patios-fixed";
        }
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

        try {
            patioService.atualizar(id, patio);
            redirectAttributes.addFlashAttribute("success", "Pátio atualizado com sucesso!");
            return "redirect:/patios-fixed";
        } catch (Exception e) {
            log.error("Erro ao atualizar pátio", e);
            model.addAttribute("error", "Erro ao atualizar pátio");
            model.addAttribute("patioId", id);
            model.addAttribute("isEdit", true);
            return "patios/form";
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patioService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Pátio removido com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao deletar pátio", e);
            redirectAttributes.addFlashAttribute("error", "Erro ao remover pátio");
        }
        return "redirect:/patios-fixed";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var patio = patioService.buscarPorId(id);
            model.addAttribute("patio", patio);
            return "patios/details";
        } catch (Exception e) {
            log.error("Erro ao carregar detalhes do pátio", e);
            redirectAttributes.addFlashAttribute("error", "Pátio não encontrado");
            return "redirect:/patios-fixed";
        }
    }
}
