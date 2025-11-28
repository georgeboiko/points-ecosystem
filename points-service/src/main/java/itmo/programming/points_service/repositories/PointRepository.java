package itmo.programming.points_service.repositories;

import itmo.programming.points_service.entities.PointEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
    List<PointEntity> findByOwnerIdOrderByIdAsc(Long ownerId);
}
