package itmo.programming.auth_service.exceptions;

public class UserAlreadyExistsException extends UserRegistrationException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
