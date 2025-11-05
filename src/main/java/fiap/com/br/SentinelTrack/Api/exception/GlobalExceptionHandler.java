package fiap.com.br.SentinelTrack.Api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn("Entidade não encontrada: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Registro não encontrado");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(PatioNotFoundException.class)
    public String handlePatioNotFound(PatioNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn("Pátio não encontrado: ID {}", e.getPatioId());
        redirectAttributes.addFlashAttribute("error", "Pátio não encontrado");
        return "redirect:/patios";
    }

    @ExceptionHandler(MotoNotFoundException.class)
    public String handleMotoNotFound(MotoNotFoundException e, RedirectAttributes redirectAttributes) {
        log.warn("Moto não encontrada: ID {}", e.getMotoId());
        redirectAttributes.addFlashAttribute("error", "Moto não encontrada");
        return "redirect:/motos";
    }

    @ExceptionHandler(DuplicatePlacaException.class)
    public String handleDuplicatePlaca(DuplicatePlacaException e, RedirectAttributes redirectAttributes) {
        log.warn("Tentativa de cadastrar placa duplicada: {}", e.getPlaca());
        redirectAttributes.addFlashAttribute("error", "Já existe uma moto com a placa " + e.getPlaca());
        return "redirect:/motos/new";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        log.error("Violação de integridade de dados", e);
        redirectAttributes.addFlashAttribute("error", "Operação não permitida. Verifique se não há registros dependentes.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public String handleValidationErrors(Exception e, Model model, RedirectAttributes redirectAttributes) {
        log.warn("Erro de validação: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Dados inválidos. Verifique os campos obrigatórios.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException e, RedirectAttributes redirectAttributes) {
        log.warn("Violação de constraint: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Dados inválidos fornecidos");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException e, RedirectAttributes redirectAttributes) {
        log.warn("Acesso negado: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Você não tem permissão para realizar esta operação");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e, RedirectAttributes redirectAttributes) {
        log.warn("Erro de negócio: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:" + e.getRedirectPath();
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception e, RedirectAttributes redirectAttributes) {
        log.error("Erro inesperado no sistema", e);
        redirectAttributes.addFlashAttribute("error", "Erro interno do sistema. Tente novamente em alguns instantes.");
        return "redirect:/dashboard";
    }
}
