package itmo.programming.points_service.dtos.requests;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointsDeleteRequestDTO {
    private List<Long> points;
}
