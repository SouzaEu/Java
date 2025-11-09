package fiap.com.br.SentinelTrack.Application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PatioDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String endereco;
    private String complemento;
    
    @Positive(message = "Área deve ser positiva")
    private BigDecimal areaM2;
    
    @NotNull(message = "ID da localidade é obrigatório")
    private Long idLocalidade;
    
    private List<MotoDTO> motos;
}
