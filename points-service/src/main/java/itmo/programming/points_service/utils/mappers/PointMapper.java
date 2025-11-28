package itmo.programming.points_service.utils.mappers;


import itmo.programming.points_service.dtos.responses.PointResponseDTO;
import itmo.programming.points_service.entities.PointEntity;
import itmo.programming.points_service.models.Point;
import org.springframework.stereotype.Component;

@Component
public class PointMapper {

    public PointEntity toEntity(Point model, Long ownerId) {
        return new PointEntity(
                model.getX(),
                model.getY(),
                model.getR(),
                model.isSuccess(),
                ownerId
        );
    }

    public Point toModel(PointEntity entity) {
        return new Point(
                entity.getId(),
                entity.getX(),
                entity.getY(),
                entity.getR(),
                entity.isSuccess(),
                entity.getOwnerId()
        );
    }

    public PointResponseDTO toResponseDTO(Point point) {
        return new PointResponseDTO(
                point.getId(),
                point.getX(),
                point.getY(),
                point.getR(),
                point.isSuccess()
        );
    }

}
