package itmo.programming.api_gateway_service.kafka.events;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationPointsDeletedEvent {
    private Long userId;
    private List<Long> points;
}
