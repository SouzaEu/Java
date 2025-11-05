package fiap.com.br.SentinelTrack.Api.exception;

import lombok.Getter;

@Getter
public class PatioNotFoundException extends RuntimeException {
    private final Long patioId;

    public PatioNotFoundException(Long patioId) {
        super("Pátio não encontrado com ID: " + patioId);
        this.patioId = patioId;
    }

    public PatioNotFoundException(String message, Long patioId) {
        super(message);
        this.patioId = patioId;
    }
}
