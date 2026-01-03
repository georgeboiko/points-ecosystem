package itmo.programming.notification_service.kafka.consumers;

import itmo.programming.notification_service.kafka.events.PointsAddedEvent;
import itmo.programming.notification_service.kafka.events.PointsDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventProducer {

    private static final String NOTIFICATION_POINTS_ADDED_TOPIC = "notification-points-added";
    private static final String NOTIFICATION_POINTS_DELETED_TOPIC = "notification-points-deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPointAddedEvent(PointsAddedEvent pointsListResponseDTO) {
        try {
            kafkaTemplate.send(NOTIFICATION_POINTS_ADDED_TOPIC, pointsListResponseDTO)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        System.out.println(exception.getMessage());
                    } else {
                        System.out.println(
                                "Message sent to topic " +
                                        result.getRecordMetadata().topic()
                        );
                    }

                });
        } catch (Exception e) {
            System.out.println("Error sending notification points added event: " + e.getMessage());
        }
    }

    public void sendPointsDeletedEvent(PointsDeletedEvent pointsDeleteResponseDTO) {
        try {
            kafkaTemplate.send(NOTIFICATION_POINTS_DELETED_TOPIC, pointsDeleteResponseDTO)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            System.out.println(exception.getMessage());
                        } else {
                            System.out.println(
                                    "Message sent to topic " +
                                            result.getRecordMetadata().topic()
                            );
                        }

                    });
        } catch (Exception e) {
            System.out.println("Error sending notification points deleted event: " + e.getMessage());
        }
    }

}
