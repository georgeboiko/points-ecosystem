package itmo.programming.points_service.exceptions;

public class NoAvailablePointsException extends RuntimeException {
    public NoAvailablePointsException(String message) {
        super(message);
    }
}
