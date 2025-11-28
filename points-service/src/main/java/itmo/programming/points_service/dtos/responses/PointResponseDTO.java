package itmo.programming.points_service.dtos.responses;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointResponseDTO {
    private Long id;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private boolean success;
}
