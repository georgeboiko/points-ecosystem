package itmo.programming.points_service.services;

import itmo.programming.points_service.dtos.requests.PointRequestDTO;
import itmo.programming.points_service.dtos.responses.InvalidPointResponseDTO;
import itmo.programming.points_service.dtos.responses.NewInvalidPointsResponseDTO;
import itmo.programming.points_service.dtos.responses.PointResponseDTO;
import itmo.programming.points_service.dtos.responses.PointsDeleteResponseDTO;
import itmo.programming.points_service.dtos.responses.PointsListResponseDTO;
import itmo.programming.points_service.entities.PointEntity;
import itmo.programming.points_service.kafka.producers.KafkaEventProducer;
import itmo.programming.points_service.models.Point;
import itmo.programming.points_service.repositories.PointRepository;
import itmo.programming.points_service.utils.PointCalculator;
import itmo.programming.points_service.utils.PointValidator;
import itmo.programming.points_service.utils.mappers.PointMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointMapper pointMapper;
    private final KafkaEventProducer kafkaEventProducer;

    public PointService(PointRepository pointRepository,
                        PointMapper pointMapper,
                        KafkaEventProducer kafkaEventProducer) {
        this.pointRepository = pointRepository;
        this.pointMapper = pointMapper;
        this.kafkaEventProducer = kafkaEventProducer;
    }


    public PointsListResponseDTO getPoints(Long ownerId) {
        List<PointEntity> points = pointRepository.findByOwnerIdOrderByIdAsc(ownerId);
        return new PointsListResponseDTO(
                points.stream()
                        .map(pointMapper::toModel)
                        .map(pointMapper::toResponseDTO)
                        .toList()
        );
    }

    public PointsListResponseDTO getPoints(Long ownerId, BigDecimal radius) {

        List<PointEntity> points = pointRepository.findByOwnerIdOrderByIdAsc(ownerId);
        return new PointsListResponseDTO(
                points.stream()
                        .map(p -> new PointResponseDTO(
                                        p.getId(),
                                        p.getX(),
                                        p.getY(),
                                        radius,
                                        PointCalculator.check(p.getX(), p.getY(), radius)
                                )
                        ).toList()
        );
    }

    public NewInvalidPointsResponseDTO addPoints(Long ownerId, List<PointRequestDTO> points) {

        List<InvalidPointResponseDTO> invalidPoints = points.stream()
                .map(p -> new InvalidPointResponseDTO(
                                p.getX(),
                                p.getY(),
                                p.getR(),
                                PointValidator.validate(p)
                        )
                )
                .filter(p -> !p.getErrors().isEmpty())
                .toList();

        List<PointEntity> calculatedPoints = points.stream()
                .filter(p -> PointValidator.validate(p).isEmpty())
                .map(p -> new Point(
                        0L,
                        p.getX(),
                        p.getY(),
                        p.getR(),
                        PointCalculator.check(p.getX(), p.getY(), p.getR()),
                        ownerId)
                )
                .map(p -> pointMapper.toEntity(p, ownerId))
                .toList();

        List<PointResponseDTO> savedPoints = pointRepository.saveAll(calculatedPoints).stream()
                .map(p -> new PointResponseDTO(p.getId(), p.getX(), p.getY(), p.getR(), p.isSuccess()))
                .toList();

        kafkaEventProducer.sendPointAddedEvent(
                new PointsListResponseDTO(
                        savedPoints
                )
        );

        return new NewInvalidPointsResponseDTO(
                invalidPoints
        );
    }

    public void deletePoints(Long ownerId, List<Long> pointsId) {

        List<PointEntity> ownerPoints = pointRepository.findByOwnerIdOrderByIdAsc(ownerId);

        List<PointEntity> pointsToDelete = ownerPoints.stream()
                .filter(p -> pointsId.contains(p.getId()))
                .toList();


        if (pointsToDelete.isEmpty()) {
            throw new NoSuchElementException("No available points");
        }

        pointRepository.deleteAllInBatch(pointsToDelete);

        kafkaEventProducer.sendPointsDeletedEvent(new PointsDeleteResponseDTO(pointsToDelete.stream().map(PointEntity::getId).toList()));
    }

}
