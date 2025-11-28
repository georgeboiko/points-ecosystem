package itmo.programming.points_service.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Point {
    private Long id;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private boolean success;
    private Long ownerId;
}
