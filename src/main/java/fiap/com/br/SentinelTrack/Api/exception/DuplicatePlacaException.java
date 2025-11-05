package fiap.com.br.SentinelTrack.Api.exception;

import lombok.Getter;

@Getter
public class DuplicatePlacaException extends RuntimeException {
    private final String placa;

    public DuplicatePlacaException(String placa) {
        super("Já existe uma moto cadastrada com a placa: " + placa);
        this.placa = placa;
    }
}
