package fiap.com.br.SentinelTrack.Application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class MotoDTO {
    private Long id;
    
    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;
    
    @NotBlank(message = "Placa é obrigatória")
    @Pattern(
        regexp = "^([A-Z]{3}[0-9]{4}|[A-Z]{3}[0-9][A-Z][0-9]{2})$",
        message = "Formato de placa inválido. Use ABC1234 ou ABC1D23"
    )
    private String placa;
    
    @NotBlank(message = "Status é obrigatório")
    private String status;
    
    @NotNull(message = "ID do pátio é obrigatório")
    private Long idPatio;
    
    private String nomePatio;
    
    @NotNull(message = "Data de entrada é obrigatória")
    private Date dataEntrada;
}
