package itmo.programming.auth_service.exceptions;

import itmo.programming.auth_service.dtos.responses.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LogOutException.class)
    public ResponseEntity<ErrorResponseDTO> handleLogOutException(
            LogOutException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        401,
                        "UNAUTHORIZED",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(RefreshException.class)
    public ResponseEntity<ErrorResponseDTO> handleRefreshException(
            RefreshException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        500,
                        "INTERNAL_SERVER_ERROR",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        409,
                        "CONFLICT",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserAuthorizationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAuthorizationException(
            UserAuthorizationException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        401,
                        "UNAUTHORIZED",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserChangeNameOrPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserChangeNameOrPasswordException(
            UserChangeNameOrPasswordException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        500,
                        "INTERNAL_SERVER_ERROR",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        404,
                        "NOT_FOUND",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserRegistrationException(
            UserRegistrationException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        500,
                        "INTERNAL_SERVER_ERROR",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }
}
