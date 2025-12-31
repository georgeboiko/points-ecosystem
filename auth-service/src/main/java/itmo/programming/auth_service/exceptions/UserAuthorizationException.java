package itmo.programming.auth_service.exceptions;

public class UserAuthorizationException extends RuntimeException {
    public UserAuthorizationException(String message) {
        super(message);
    }

    public UserAuthorizationException(String message, Throwable e) {
        super(message, e);
    }
}
