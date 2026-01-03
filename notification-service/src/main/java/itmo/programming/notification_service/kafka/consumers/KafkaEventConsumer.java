package itmo.programming.notification_service.kafka.consumers;

import itmo.programming.notification_service.kafka.events.PointsAddedEvent;
import itmo.programming.notification_service.kafka.events.PointsDeletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private static final String POINTS_ADDED_TOPIC = "points-added";
    private static final String POINTS_DELETED_TOPIC = "points-deleted";

    @KafkaListener(topics = POINTS_ADDED_TOPIC)
    public void consumeAddedPoints(PointsAddedEvent pointsAddedEvent) {
        System.out.println(pointsAddedEvent.toString());
    }

    @KafkaListener(topics = POINTS_DELETED_TOPIC)
    public void consumeDeletedPoints(PointsDeletedEvent pointsDeletedEvent) {
        System.out.println(pointsDeletedEvent.toString());
    }

}
