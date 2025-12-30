package itmo.programming.points_service.dtos.responses;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponseDTO {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
