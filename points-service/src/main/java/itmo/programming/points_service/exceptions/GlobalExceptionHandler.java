package itmo.programming.points_service.exceptions;

import itmo.programming.points_service.dtos.responses.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoAvailablePointsException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoAvailablePoints(
            NoAvailablePointsException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(
                        Instant.now(),
                        404,
                        "NOT FOUND",
                        exception.getMessage(),
                        request.getRequestURI()
                ));
    }

}
