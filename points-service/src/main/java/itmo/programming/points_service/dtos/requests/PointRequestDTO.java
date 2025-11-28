package itmo.programming.points_service.dtos.requests;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointRequestDTO {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
}
