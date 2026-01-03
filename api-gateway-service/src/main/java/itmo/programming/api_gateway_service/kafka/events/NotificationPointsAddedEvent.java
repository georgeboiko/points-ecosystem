package itmo.programming.api_gateway_service.kafka.events;

import itmo.programming.api_gateway_service.dtos.PointDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationPointsAddedEvent {
    private Long userId;
    private List<PointDTO> validPoints;
}