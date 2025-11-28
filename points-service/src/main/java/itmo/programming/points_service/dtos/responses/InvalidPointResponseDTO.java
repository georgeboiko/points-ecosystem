package itmo.programming.points_service.dtos.responses;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidPointResponseDTO {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private List<String> errors;
}
