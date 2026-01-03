package itmo.programming.api_gateway_service.kafka.consumers;

import itmo.programming.api_gateway_service.kafka.events.NotificationPointsAddedEvent;
import itmo.programming.api_gateway_service.kafka.events.NotificationPointsDeletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private static final String NOTIFICATION_POINTS_ADDED_TOPIC = "notification-points-added";
    private static final String NOTIFICATION_POINTS_DELETED_TOPIC = "notification-points-deleted";

    @KafkaListener(
            topics = NOTIFICATION_POINTS_ADDED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.api_gateway_service.kafka.events.NotificationPointsAddedEvent"
            }
    )
    public void consumeAddedPoints(NotificationPointsAddedEvent notificationPointsAddedEvent) {
        System.out.println(notificationPointsAddedEvent.toString());
    }

    @KafkaListener(
            topics = NOTIFICATION_POINTS_DELETED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.api_gateway_service.kafka.events.NotificationPointsDeletedEvent"
            }
    )
    public void consumeDeletedPoints(NotificationPointsDeletedEvent notificationPointsDeletedEvent) {
        System.out.println(notificationPointsDeletedEvent.toString());
    }

}