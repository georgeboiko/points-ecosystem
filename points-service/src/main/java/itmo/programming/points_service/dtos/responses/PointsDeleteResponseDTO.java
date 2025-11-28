package itmo.programming.points_service.dtos.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointsDeleteResponseDTO {
    private List<Long> points;
}
