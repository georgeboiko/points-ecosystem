package itmo.programming.points_service.repositories;

import itmo.programming.points_service.entities.PointEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
    List<PointEntity> findByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("SELECT p FROM PointEntity p WHERE p.id IN :ids AND p.ownerId = :ownerId")
    List<PointEntity> findByDeleteIdInAndOwnerId(@Param("ids") List<Long> ids,
                                           @Param("ownerId") Long ownerId);
}
