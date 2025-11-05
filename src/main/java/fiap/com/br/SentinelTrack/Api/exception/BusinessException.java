package fiap.com.br.SentinelTrack.Api.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String redirectPath;

    public BusinessException(String message, String redirectPath) {
        super(message);
        this.redirectPath = redirectPath;
    }

    public BusinessException(String message, String redirectPath, Throwable cause) {
        super(message, cause);
        this.redirectPath = redirectPath;
    }
}
