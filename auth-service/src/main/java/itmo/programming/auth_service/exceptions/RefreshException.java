package itmo.programming.auth_service.exceptions;

public class RefreshException extends RuntimeException {
    public RefreshException(String message) {
        super(message);
    }

    public RefreshException(String message, Throwable e) {
        super(message, e);
    }
}
