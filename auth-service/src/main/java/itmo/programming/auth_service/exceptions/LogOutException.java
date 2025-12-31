package itmo.programming.auth_service.exceptions;

public class LogOutException extends RuntimeException {
    public LogOutException(String message, Throwable e) {
        super(message, e);
    }
}
