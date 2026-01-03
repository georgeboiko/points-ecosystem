package itmo.programming.notification_service.kafka.events;

import itmo.programming.notification_service.dtos.PointDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PointsAddedEvent {
    private Long userId;
    private List<PointDTO> validPoints;
}
