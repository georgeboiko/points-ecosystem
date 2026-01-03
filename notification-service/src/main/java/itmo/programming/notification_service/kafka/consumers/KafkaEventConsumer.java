package itmo.programming.notification_service.kafka.consumers;

import itmo.programming.notification_service.kafka.events.PointsAddedEvent;
import itmo.programming.notification_service.kafka.events.PointsDeletedEvent;
import itmo.programming.notification_service.kafka.producers.KafkaEventProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private static final String POINTS_ADDED_TOPIC = "points-added";
    private static final String POINTS_DELETED_TOPIC = "points-deleted";

    private final KafkaEventProducer kafkaEventProducer;

    public KafkaEventConsumer(KafkaEventProducer kafkaEventProducer) {
        this.kafkaEventProducer = kafkaEventProducer;
    }

    @KafkaListener(
            topics = POINTS_ADDED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.notification_service.kafka.events.PointsAddedEvent"
            }
    )
    public void consumeAddedPoints(PointsAddedEvent pointsAddedEvent) {
        kafkaEventProducer.sendPointAddedEvent(pointsAddedEvent);
    }

    @KafkaListener(
            topics = POINTS_DELETED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.notification_service.kafka.events.PointsDeletedEvent"
            }
    )
    public void consumeDeletedPoints(PointsDeletedEvent pointsDeletedEvent) {
        kafkaEventProducer.sendPointsDeletedEvent(pointsDeletedEvent);
    }

}
