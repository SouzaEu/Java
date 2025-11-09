package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.services.PatioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patios")
public class PatioWebController {

    private final PatioService patioService;

    public PatioWebController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
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
            model.addAttribute("isEdit", false);
            return "patios/form";
        }

        try {
            patioService.criar(patio);
            redirectAttributes.addFlashAttribute("success", "Pátio criado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar pátio: " + e.getMessage());
            model.addAttribute("isEdit", false);
            return "patios/form";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var patioOpt = patioService.buscarPorId(id);
        if (patioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pátio não encontrado!");
            return "redirect:/patios";
        }

        var patio = patioOpt.get();
        var createDTO = new CreatePatioDTO();
        createDTO.setNome(patio.getNome());
        createDTO.setEndereco(patio.getEndereco());
        createDTO.setComplemento(patio.getComplemento());
        createDTO.setAreaM2(patio.getAreaM2());
        createDTO.setIdLocalidade(patio.getIdLocalidade());

        model.addAttribute("patio", createDTO);
        model.addAttribute("patioId", id);
        model.addAttribute("isEdit", true);
        return "patios/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String atualizar(@PathVariable Long id,
                           @Valid @ModelAttribute("patio") CreatePatioDTO patio,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patioId", id);
            model.addAttribute("isEdit", true);
            return "patios/form";
        }

        try {
            var updated = patioService.atualizar(id, patio);
            if (updated.isPresent()) {
                redirectAttributes.addFlashAttribute("success", "Pátio atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Pátio não encontrado!");
            }
            return "redirect:/patios";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar pátio: " + e.getMessage());
            model.addAttribute("patioId", id);
            model.addAttribute("isEdit", true);
            return "patios/form";
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (patioService.deletar(id)) {
                redirectAttributes.addFlashAttribute("success", "Pátio removido com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Pátio não encontrado!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao remover pátio: " + e.getMessage());
        }
        return "redirect:/patios";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var patioOpt = patioService.buscarPorId(id);
        if (patioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pátio não encontrado!");
            return "redirect:/patios";
        }

        model.addAttribute("patio", patioOpt.get());
        return "patios/details";
    }
}
