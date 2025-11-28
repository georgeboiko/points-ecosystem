package itmo.programming.points_service.kafka.producers;

import itmo.programming.points_service.dtos.responses.PointsDeleteResponseDTO;
import itmo.programming.points_service.dtos.responses.PointsListResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventProducer {

    private static final String POINTS_ADDED_TOPIC = "points-added";
    private static final String POINTS_DELETED_TOPIC = "points-deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPointAddedEvent(PointsListResponseDTO pointsListResponseDTO) {
        try {
            kafkaTemplate.send(POINTS_ADDED_TOPIC, pointsListResponseDTO)
                .whenComplete((result, exception) -> {

                });
        } catch (Exception e) {
            System.out.println(String.format("Error sending points added event: {}", e.getMessage(), e));
        }
    }

    public void sendPointsDeletedEvent(PointsDeleteResponseDTO pointsDeleteResponseDTO) {
        try {
            kafkaTemplate.send(POINTS_DELETED_TOPIC, pointsDeleteResponseDTO)
                .whenComplete((result, exception) -> {

                });
        } catch (Exception e) {
            System.out.println(String.format("Error sending points deleted event: {}", e.getMessage(), e));
        }
    }

}
