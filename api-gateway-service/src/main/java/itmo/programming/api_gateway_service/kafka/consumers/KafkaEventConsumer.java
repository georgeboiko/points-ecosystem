package itmo.programming.api_gateway_service.kafka.consumers;

import itmo.programming.api_gateway_service.kafka.events.NotificationPointsAddedEvent;
import itmo.programming.api_gateway_service.kafka.events.NotificationPointsDeletedEvent;
import itmo.programming.api_gateway_service.websocket.NotificationHandlers.PointsAddedWebSocketHandler;
import itmo.programming.api_gateway_service.websocket.NotificationHandlers.PointsDeletedWebSocketHandler;
import itmo.programming.api_gateway_service.websocket.WebSocketSessionRegistry;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaEventConsumer {

    private static final String NOTIFICATION_POINTS_ADDED_TOPIC = "notification-points-added";
    private static final String NOTIFICATION_POINTS_DELETED_TOPIC = "notification-points-deleted";

    private final PointsAddedWebSocketHandler addedHandler;
    private final PointsDeletedWebSocketHandler deletedHandler;
    private final ObjectMapper mapper = new ObjectMapper();

    public KafkaEventConsumer(
            PointsAddedWebSocketHandler addedHandler,
            PointsDeletedWebSocketHandler deletedHandler
    ) {
        this.addedHandler = addedHandler;
        this.deletedHandler = deletedHandler;
    }

    @KafkaListener(
            topics = NOTIFICATION_POINTS_ADDED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.api_gateway_service.kafka.events.NotificationPointsAddedEvent"
            }
    )
    public void consumeAddedPoints(NotificationPointsAddedEvent notificationPointsAddedEvent) {
        send(
                notificationPointsAddedEvent.getUserId(),
                mapper.writeValueAsString(notificationPointsAddedEvent),
                addedHandler.getRegistry()
        );
    }

    @KafkaListener(
            topics = NOTIFICATION_POINTS_DELETED_TOPIC,
            properties = {
                    "spring.json.value.default.type=itmo.programming.api_gateway_service.kafka.events.NotificationPointsDeletedEvent"
            }
    )
    public void consumeDeletedPoints(NotificationPointsDeletedEvent notificationPointsDeletedEvent) {
        send(
                notificationPointsDeletedEvent.getUserId(),
                mapper.writeValueAsString(notificationPointsDeletedEvent),
                deletedHandler.getRegistry()
        );
    }

    private void send(Long userId, String payload, WebSocketSessionRegistry registry) {
        registry.get(userId).forEach(session ->
                session.send(
                        Mono.just(session.textMessage(payload))
                ).subscribe()
        );
    }

}