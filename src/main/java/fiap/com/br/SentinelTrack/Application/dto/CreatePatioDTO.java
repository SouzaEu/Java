package fiap.com.br.SentinelTrack.Application.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePatioDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\-\\.]+$", message = "Nome contém caracteres inválidos")
    private String nome;
    
    @Size(max = 255, message = "Endereço não pode exceder 255 caracteres")
    private String endereco;
    
    @Size(max = 100, message = "Complemento não pode exceder 100 caracteres")
    private String complemento;
    
    @DecimalMin(value = "1.0", message = "Área deve ser maior que 1m²")
    @DecimalMax(value = "999999.99", message = "Área muito grande")
    @Digits(integer = 6, fraction = 2, message = "Formato de área inválido")
    private BigDecimal areaM2;
    
    @NotNull(message = "ID da localidade é obrigatório")
    @Positive(message = "ID da localidade deve ser positivo")
    private Long idLocalidade;
}
