package itmo.programming.notification_service.kafka.events;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointsDeletedEvent {
    private Long userId;
    private List<Long> points;
}
