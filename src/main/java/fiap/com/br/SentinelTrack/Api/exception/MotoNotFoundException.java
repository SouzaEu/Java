package fiap.com.br.SentinelTrack.Api.exception;

import lombok.Getter;

@Getter
public class MotoNotFoundException extends RuntimeException {
    private final Long motoId;

    public MotoNotFoundException(Long motoId) {
        super("Moto não encontrada com ID: " + motoId);
        this.motoId = motoId;
    }

    public MotoNotFoundException(String message, Long motoId) {
        super(message);
        this.motoId = motoId;
    }
}
