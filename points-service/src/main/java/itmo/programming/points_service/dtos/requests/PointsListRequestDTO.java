package itmo.programming.points_service.dtos.requests;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointsListRequestDTO {
    private List<PointRequestDTO> points;
}
