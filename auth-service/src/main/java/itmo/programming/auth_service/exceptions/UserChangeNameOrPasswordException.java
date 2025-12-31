package itmo.programming.auth_service.exceptions;

public class UserChangeNameOrPasswordException extends RuntimeException {
    public UserChangeNameOrPasswordException(String message) {
        super(message);
    }
}
