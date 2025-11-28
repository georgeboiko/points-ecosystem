package itmo.programming.points_service.dtos.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointsListResponseDTO {
    private List<PointResponseDTO> validPoints;
}
